package com.buildguard.entity;

import jakarta.persistence.Entity;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IotDevice extends BaseEntity {
    private Long projectId;
    private Long businessDeviceId;
    private String deviceCode;
    private String deviceName;
    private String deviceType;
    private String vendorName;
    private String apiKey;
    private String apiSecret;
    private String status;
    private Boolean pendingBind;
    private LocalDateTime lastOnlineTime;
    private LocalDateTime lastMessageTime;
    private String lastIp;
    private String remark;
}
