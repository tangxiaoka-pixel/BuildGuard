package com.buildguard.entity;

import jakarta.persistence.Entity;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Device extends BaseEntity {
    private Long projectId;
    private Long areaId;
    private String deviceName;
    private String deviceNo;
    private String vendorName;
    private String deviceModel;
    private String brand;
    private String model;
    private String deviceType;
    private String apiKey;
    private String location;
    private String ipAddress;
    private String status;
    private Boolean pendingConfirm;
    private LocalDateTime lastSigninTime;
    private LocalDateTime lastHeartbeatTime;
    private LocalDateTime lastAttendanceTime;
    private String lastIp;
    private String remark;
    private LocalDateTime lastOnlineTime;
}
