package com.buildguard.entity;

import jakarta.persistence.Entity;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EducationExam extends BaseEntity {
    private Long workerId;
    private Long projectId;
    private Long courseId;
    private String workerName;
    private String courseName;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private Integer score;
    private Boolean passed;
    private String answerDetail;
    private String validStartDate;
    private String validEndDate;
}
