package com.buildguard.entity;

import jakarta.persistence.Entity;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TowerCrane extends BaseEntity {
    private Long projectId;
    private Long iotDeviceId;
    private String craneName;
    private String craneNo;
    private String recordNo;
    private String propertyUnit;
    private String installUnit;
    private String inspectionUnit;
    private String installLocation;
    private String driverName;
    private String managerName;
    private String status;
    private LocalDateTime installDate;
    private LocalDateTime dismantleDate;
    private String remark;
}
