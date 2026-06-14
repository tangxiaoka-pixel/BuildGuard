package com.buildguard.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Company extends BaseEntity {
    private String name;
    private String creditCode;
    private String type;
    private String contactName;
    private String contactPhone;
    private String address;
    private String status;
    private String remark;
}
