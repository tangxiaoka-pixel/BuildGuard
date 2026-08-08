package com.buildguard.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProjectSpatialPoint extends BaseEntity {
    private Long projectId;
    private Long assetId;
    private String pointType;
    private Long businessDeviceId;
    private String title;
    @JsonProperty("xPercent")
    private Double xPercent;
    @JsonProperty("yPercent")
    private Double yPercent;
    @JsonProperty("zPercent")
    private Double zPercent;
    private String bindingMode;
    private String componentKey;
    private String componentName;
    private Double anchorX;
    private Double anchorY;
    private Double anchorZ;
    private String status;
    private String remark;
}
