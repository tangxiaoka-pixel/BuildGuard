package com.buildguard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IotOpenApiLog extends BaseEntity {
    private Long projectId;
    private String deviceCode;
    private String interfaceName;
    private String requestIp;
    private Boolean success;
    private String message;
    @Lob @Column(columnDefinition = "TEXT")
    private String requestBody;
    @Lob @Column(columnDefinition = "TEXT")
    private String responseBody;
}
