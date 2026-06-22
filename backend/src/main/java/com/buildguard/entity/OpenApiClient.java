package com.buildguard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OpenApiClient extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String apiKey;
    @Column(nullable = false)
    private String apiSecret;
    private String vendorName;
    private String contactName;
    private String contactPhone;
    private String status;
    private String remark;
}
