package com.buildguard.entity;

import jakarta.persistence.Entity;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProjectSpatialAsset extends BaseEntity {
    private Long projectId;
    private Long fileResourceId;
    private String assetType;
    private String name;
    private String fileUrl;
    private String fileName;
    private String contentType;
    private LocalDateTime uploadedAt;
    private String remark;
}
