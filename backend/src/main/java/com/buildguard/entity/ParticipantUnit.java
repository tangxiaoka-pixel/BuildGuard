package com.buildguard.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParticipantUnit extends BaseEntity {
    private Long projectId;
    private String unitName;
    private String creditCode;
    private String unitType;
    private Long parentUnitId;
    private String contactName;
    private String contactPhone;
    private Boolean isPrimary;
    private String status;
}
