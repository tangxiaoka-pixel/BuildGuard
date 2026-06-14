package com.buildguard.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EducationLedger extends BaseEntity {
    private Long workerId;
    private Long projectId;
    private Long participantUnitId;
    private Long teamId;
    private String workerName;
    private String workType;
    private String educationType;
    private String educationMethod;
    private String courseName;
    private String educationDate;
    private String educationLocation;
    private String teacher;
    private Double classHours;
    private Integer score;
    private Boolean passed;
    private String validStartDate;
    private String validEndDate;
    private String attachmentUrl;
    private String remark;
}
