package com.buildguard.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TrainingTask extends BaseEntity {
    private Long projectId;
    private Long courseId;
    private String name;
    private String taskToken;
    private String startTime;
    private String endTime;
    private String targetType;
    private String targetIds;
    private String status;
    private String remark;
}
