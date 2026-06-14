package com.buildguard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EducationQuestion extends BaseEntity {
    private Long projectId;
    /** Legacy field kept for automatic migration from the original course-owned model. */
    private Long courseId;
    private String questionContent;
    private String questionType;
    @Column(length = 2000) private String options;
    private String correctAnswer;
    private Integer score;
    private String analysis;
    private String status;
}
