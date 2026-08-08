package com.buildguard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "device_offline_event")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DeviceOfflineEvent extends BaseEntity {
    private Long projectId;
    private Long deviceId;
    private String deviceType;
    private String deviceName;
    private String deviceCode;
    private String status;
    private LocalDateTime offlineAt;
    private LocalDateTime recoveredAt;
    private String handler;
    private LocalDateTime handledAt;
    private String handleRemark;
}
