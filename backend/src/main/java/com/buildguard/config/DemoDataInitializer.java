package com.buildguard.config;

import com.buildguard.entity.PlatformUser;
import com.buildguard.entity.ScopeRole;
import com.buildguard.entity.Tenant;
import com.buildguard.repository.CompanyRepository;
import com.buildguard.repository.OpenApiClientRepository;
import com.buildguard.repository.PlatformUserRepository;
import com.buildguard.repository.ProjectRepository;
import com.buildguard.repository.ScopeRoleRepository;
import com.buildguard.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.LinkedHashSet;

@Configuration
@RequiredArgsConstructor
public class DemoDataInitializer {
    private final TenantRepository tenants;
    private final PlatformUserRepository platformUsers;
    private final CompanyRepository companies;
    private final ProjectRepository projects;
    private final ScopeRoleRepository scopeRoles;
    private final OpenApiClientRepository openApiClients;

    @Bean
    CommandLineRunner systemAccountInitializer() {
        return args -> {
            Tenant tenant = tenants.findAll().stream().findFirst().orElseGet(() ->
                    tenants.save(Tenant.builder()
                            .tenantName("系统默认空间")
                            .tenantCode("SYSTEM")
                            .status("ACTIVE")
                            .build()));

            PlatformUser admin = platformUsers.findByUsername("admin").orElseGet(() -> {
                PlatformUser user = PlatformUser.builder()
                        .name("平台超级管理员")
                        .phone("13900000000")
                        .username("admin")
                        .role("SUPER_ADMIN")
                        .status("ACTIVE")
                        .build();
                user.setTenantId(tenant.getId());
                return user;
            });
            if (admin.getPassword() == null || !admin.getPassword().startsWith("$2")) {
                admin.setPassword(new BCryptPasswordEncoder().encode("123456"));
            }
            platformUsers.save(admin);

            platformUsers.findAll().stream()
                    .filter(user -> "COMPANY_ADMIN".equals(user.getRole()))
                    .forEach(user -> {
                        user.setAllProjectCompanyIds(new LinkedHashSet<>(user.getCompanyIds()));
                        user.setProjectIds(new LinkedHashSet<>());
                        platformUsers.save(user);
                    });
            companies.findAll().forEach(company -> seedRoles("COMPANY", company.getId()));
            projects.findAll().forEach(project -> seedRoles("PROJECT", project.getId()));
            openApiClients.findByApiKey("buildguard-demo").orElseGet(() -> openApiClients.save(com.buildguard.entity.OpenApiClient.builder()
                    .apiKey("buildguard-demo")
                    .apiSecret("buildguard-secret")
                    .vendorName("演示设备厂家")
                    .contactName("联调联系人")
                    .contactPhone("13800000000")
                    .status("ACTIVE")
                    .remark("默认联调账号，可在开放接入页面停用或修改")
                    .build()));
        };
    }

    private void seedRoles(String type, Long scopeId) {
        if (!scopeRoles.findByScopeTypeAndScopeId(type, scopeId).isEmpty()) return;
        String all = "*";
        scopeRoles.save(ScopeRole.builder().scopeType(type).scopeId(scopeId).name("管理员").status("ACTIVE").systemRole(true).moduleKeys(new LinkedHashSet<>(java.util.Set.of(all))).build());
        java.util.Set<String> modules = "COMPANY".equals(type) ? java.util.Set.of("companySummary") : java.util.Set.of("dashboard");
        scopeRoles.save(ScopeRole.builder().scopeType(type).scopeId(scopeId).name("运营人员").status("ACTIVE").systemRole(true).moduleKeys(new LinkedHashSet<>(modules)).build());
    }
}
