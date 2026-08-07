package com.buildguard.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MonitorData extends BaseEntity {
    private Long projectId;
    private Long monitorDeviceId;
    private Long iotDeviceId;
    private String monitorType;
    private String deviceCode;
    private String status;
    private String labelA;
    private Double valueA;
    private String unitA;
    private String labelB;
    private Double valueB;
    private String unitB;
    private String labelC;
    private Double valueC;
    private String unitC;
    private String labelD;
    private Double valueD;
    private String unitD;
    private String labelE;
    private Double valueE;
    private String unitE;
    private String labelF;
    private Double valueF;
    private String unitF;
    private String eventType;
    private String eventMessage;
    private String snapshotUrl;
    private LocalDateTime collectedAt;
    @Lob @Column(columnDefinition = "TEXT")
    private String rawData;
}
