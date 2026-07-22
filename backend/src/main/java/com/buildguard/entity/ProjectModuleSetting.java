package com.buildguard.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProjectModuleSetting extends BaseEntity {
    private Long projectId;
    private String moduleCode;
    private String moduleName;
    private Boolean enabled;
}
