package com.buildguard.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TowerCraneData extends BaseEntity {
    private Long projectId;
    private Long towerCraneId;
    private Long iotDeviceId;
    private String deviceCode;
    private Double loadWeight;
    private Double amplitude;
    private Double height;
    private Double rotationAngle;
    private Double tiltAngle;
    private Double windSpeed;
    private Double torquePercent;
    private Double loadPercent;
    private Integer liftingCount;
    private String status;
    private LocalDateTime collectedAt;
    @Lob @Column(columnDefinition = "TEXT")
    private String rawData;
}
