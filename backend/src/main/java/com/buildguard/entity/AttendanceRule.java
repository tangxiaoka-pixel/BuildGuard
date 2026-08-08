package com.buildguard.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AttendanceRule extends BaseEntity {
    private Long projectId;
    private String name;
    private String workStartTime;
    private String workEndTime;
    private Integer lateGraceMinutes;
    private Integer earlyGraceMinutes;
    private Integer overtimeThresholdMinutes;
    private Boolean crossDay;
    private String status;
}
