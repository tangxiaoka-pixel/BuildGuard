package com.buildguard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "platform_tenant")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Tenant extends BaseEntity {
    private String tenantName;
    private String tenantCode;
    private String creditCode;
    private String contactName;
    private String contactPhone;
    private String status;
}
