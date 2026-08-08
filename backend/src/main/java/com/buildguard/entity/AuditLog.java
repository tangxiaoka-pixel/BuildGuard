package com.buildguard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "audit_log")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditLog extends BaseEntity {
    private Long actorId;
    private String actorRole;
    private String action;
    private String requestMethod;
    private String requestPath;
    private Long companyId;
    private Long projectId;
    private String requestIp;
    private Integer responseStatus;
    private Boolean success;
    @Lob
    private String message;
}
