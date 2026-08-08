package com.buildguard.service;

import com.buildguard.entity.AuditLog;
import com.buildguard.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {
    private final AuditLogRepository logs;
    private final AuthService authService;

    public void write(HttpServletRequest request, int status, String message) {
        if ("GET".equalsIgnoreCase(request.getMethod()) || request.getRequestURI().startsWith("/api/open/")) return;
        AuthService.AuthScope scope;
        try { scope = authService.require(request); } catch (RuntimeException ignored) { return; }
        logs.save(AuditLog.builder()
                .actorId(scope.userId()).actorRole(scope.role())
                .action(action(request)).requestMethod(request.getMethod()).requestPath(request.getRequestURI())
                .requestIp(clientIp(request)).responseStatus(status).success(status < 400)
                .message(message).build());
    }

    private String action(HttpServletRequest request) {
        return switch (request.getMethod().toUpperCase()) {
            case "POST" -> "CREATE_OR_EXECUTE";
            case "PUT", "PATCH" -> "UPDATE";
            case "DELETE" -> "DELETE";
            default -> request.getMethod().toUpperCase();
        };
    }

    private String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        return forwarded == null || forwarded.isBlank() ? request.getRemoteAddr() : forwarded.split(",")[0].trim();
    }
}
