package com.buildguard.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ExternalWorkerMapping extends BaseEntity {
    private Long projectId;
    private String platformType;
    private String externalEmpId;
    private Long workerId;
    private String idCardNo;
    private String workerName;
    private String deviceNo;
    private String source;
}
