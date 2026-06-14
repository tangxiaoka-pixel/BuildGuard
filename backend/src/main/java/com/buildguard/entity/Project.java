package com.buildguard.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Project extends BaseEntity {
    private Long companyId;
    private String projectName;
    private String projectCode;
    private String address;
    private String supervisionUnit;
    private String managerName;
    private String managerPhone;
    private String startDate;
    private String endDate;
    private String status;
    private String remark;
}
