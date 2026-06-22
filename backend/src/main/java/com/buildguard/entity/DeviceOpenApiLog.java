package com.buildguard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DeviceOpenApiLog extends BaseEntity {
    private String deviceNo;
    private String apiKey;
    private String interfaceName;
    @Lob
    private String requestQuery;
    @Lob
    private String requestBody;
    @Lob
    private String responseBody;
    private Boolean success;
    private String errorCode;
    private String errorMessage;
    private String requestIp;
}
