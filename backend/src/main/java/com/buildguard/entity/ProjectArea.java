package com.buildguard.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProjectArea extends BaseEntity {
    private Long projectId;
    private String areaName;
    private String areaCode;
    private String areaType;
    private Long parentAreaId;
    private String managerName;
    private Boolean isControlled;
    private Boolean requireSafetyEducation;
    private Boolean isDangerArea;
    private String status;
}
