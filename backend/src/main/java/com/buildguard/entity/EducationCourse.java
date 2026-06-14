package com.buildguard.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EducationCourse extends BaseEntity {
    private Long projectId;
    private String name;
    private String courseType;
    private String content;
    private Integer passScore;
    private Integer validDays;
    private String status;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "education_course_question", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "question_id")
    @Builder.Default
    private Set<Long> questionIds = new LinkedHashSet<>();
}
