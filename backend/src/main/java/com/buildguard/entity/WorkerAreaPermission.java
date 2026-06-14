package com.buildguard.entity;

import jakarta.persistence.Entity;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WorkerAreaPermission extends BaseEntity {
    private Long projectId;
    private Long workerId;
    private Long areaId;
    private String permissionStatus;
    private String deviceSyncStatus;
    private LocalDateTime validStartTime;
    private LocalDateTime validEndTime;
    private LocalDateTime lastSyncTime;
    private String lastSyncResult;
}
