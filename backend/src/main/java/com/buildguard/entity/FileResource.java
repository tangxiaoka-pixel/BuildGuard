package com.buildguard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "file_resource")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FileResource extends BaseEntity {
    private Long companyId;
    private Long projectId;
    private String fileKey;
    private String originalName;
    private String storedName;
    private String extension;
    private String contentType;
    private Long sizeBytes;
    private String fileCategory;
    private String sensitivityLevel;
    private String businessType;
    private Long businessId;
    private Long uploadedBy;
    private Boolean deleted;
    private Long deletedBy;
    private java.time.LocalDateTime deletedAt;
    @Lob
    private String remark;
}
