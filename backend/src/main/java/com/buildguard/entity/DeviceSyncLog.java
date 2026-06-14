package com.buildguard.entity;

import jakarta.persistence.Entity;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DeviceSyncLog extends BaseEntity {
    private Long projectId;
    private Long areaId;
    private Long deviceId;
    private Long workerId;
    private String workerName;
    private String deviceName;
    private String actionType;
    private String syncStatus;
    private String errorMessage;
    private Integer retryCount;
    private LocalDateTime syncedAt;
}
