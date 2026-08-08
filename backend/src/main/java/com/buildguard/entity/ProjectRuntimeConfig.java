package com.buildguard.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProjectRuntimeConfig extends BaseEntity {
    private Long projectId;
    private Integer offlineThresholdMinutes;
    private Integer educationValidDays;
    private Boolean h5WorkerEntryEnabled;
    private Boolean requireFacePhoto;
    private Boolean requireSafetyEducationForAccess;
    private String remark;
}
