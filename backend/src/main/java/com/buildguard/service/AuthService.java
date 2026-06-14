package com.buildguard.service;

import com.buildguard.entity.PlatformUser;
import com.buildguard.exception.BusinessException;
import com.buildguard.repository.PlatformUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PlatformUserRepository users;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Map<String, AuthScope> sessions = new ConcurrentHashMap<>();

    public LoginResult login(String username, String password) {
        PlatformUser user = users.findByPhone(username).or(() -> users.findByUsername(username))
                .orElseThrow(() -> new BusinessException("账号或密码错误"));
        if (!"ACTIVE".equals(user.getStatus()) || !matches(password, user.getPassword())) {
            throw new BusinessException("账号或密码错误");
        }
        if (!Set.of("SUPER_ADMIN", "COMPANY_ADMIN", "COMPANY_USER").contains(user.getRole())) {
            throw new BusinessException("该账号角色已停用，请联系平台超级管理员");
        }
        migratePassword(user, password);
        user.setLastLoginTime(LocalDateTime.now());
        users.save(user);
        String token = UUID.randomUUID().toString();
        AuthScope scope = new AuthScope(user.getId(), user.getRole(), Set.copyOf(user.getCompanyIds()), Set.copyOf(user.getAllProjectCompanyIds()), Set.copyOf(user.getProjectIds()));
        sessions.put(token, scope);
        return new LoginResult(token, user, scope);
    }

    public AuthScope require(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            AuthScope scope = sessions.get(header.substring(7));
            if (scope != null) return scope;
        }
        throw new BusinessException("登录状态已失效，请重新登录");
    }

    public void logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) sessions.remove(header.substring(7));
    }

    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean verify(String rawPassword, String storedPassword) {
        return matches(rawPassword, storedPassword);
    }

    private boolean matches(String rawPassword, String storedPassword) {
        return storedPassword != null && (storedPassword.startsWith("$2") ? passwordEncoder.matches(rawPassword, storedPassword) : Objects.equals(rawPassword, storedPassword));
    }

    private void migratePassword(PlatformUser user, String rawPassword) {
        if (user.getPassword() == null || !user.getPassword().startsWith("$2")) user.setPassword(encode(rawPassword));
    }

    public record LoginResult(String token, PlatformUser user, AuthScope scope) {}
    public record AuthScope(Long userId, String role, Set<Long> companyIds, Set<Long> allProjectCompanyIds, Set<Long> projectIds) {
        public boolean superAdmin() { return "SUPER_ADMIN".equals(role); }
        public boolean companyAdmin() { return "COMPANY_ADMIN".equals(role); }
        public boolean canAccessCompany(Long companyId) { return superAdmin() || companyIds.contains(companyId); }
        public boolean canAccessProject(Long companyId, Long projectId) { return superAdmin() || allProjectCompanyIds.contains(companyId) || projectIds.contains(projectId); }
    }
}
