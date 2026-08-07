package com.buildguard.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MonitorAlarm extends BaseEntity {
    private Long projectId;
    private Long monitorDeviceId;
    private Long iotDeviceId;
    private String monitorType;
    private String deviceCode;
    private String alarmType;
    private String alarmLevel;
    private String alarmMessage;
    private String handleStatus;
    private String handler;
    private LocalDateTime handledAt;
    private String handleRemark;
    private LocalDateTime alarmTime;
    @Lob @Column(columnDefinition = "TEXT")
    private String rawData;
}
