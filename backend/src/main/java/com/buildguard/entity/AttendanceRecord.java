package com.buildguard.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AttendanceRecord extends BaseEntity {
    private Long workerId;
    private Long projectId;
    private Long areaId;
    private Long deviceId;
    private String workerName;
    private String deviceName;
    private String areaName;
    private String direction;
    private String verifyType;
    private Double score;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime attendanceTime;
    private String accessResult;
    private String denyReason;
    private String reportStatus;
}
