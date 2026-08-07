package com.buildguard.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MonitorDevice extends BaseEntity {
    private Long projectId;
    private Long iotDeviceId;
    private String monitorType;
    private String deviceName;
    private String deviceNo;
    private String installLocation;
    private String vendorName;
    private String deviceModel;
    private String status;
    private String remark;
}
