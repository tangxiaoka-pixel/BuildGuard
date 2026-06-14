package com.buildguard.entity;

import jakarta.persistence.Entity;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReportLog extends BaseEntity {
    private String businessType;
    private Long businessId;
    private String businessName;
    private String status;
    private String errorMessage;
    private Integer retryCount;
    private LocalDateTime submittedAt;
}
