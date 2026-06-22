package com.buildguard.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Worker extends BaseEntity {
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String personCode;
    @Column(nullable = false) private String idCardNo;
    private String phone;
    private String gender;
    private String nation;
    private String birthDate;
    private String address;
    private String workType;
    private String skillLevel;
    private Long projectId;
    private Long teamId;
    private Long participantUnitId;
    private String workerType;
    private String status;
    private String entryDate;
    private String exitDate;
    private String safetyEducationStatus;
    private String accessStatus;
    private String deviceSyncStatus;
    private String qrToken;
    @Lob
    private String avatarUrl;
    @Lob
    private String idCardPhotoUrl;
    @Lob
    private String idCardBackPhotoUrl;
    private String bankCardNo;
    private String emergencyContact;
    private String emergencyPhone;
    private String contractStatus;
    private String wageStatus;
    private String realNameReportStatus;
    private LocalDateTime realNameReportedAt;
    private String realNameReportResult;
    private String source;
    private String externalPlatform;
    private String externalEmpId;
    private Boolean autoCreated;
    private Boolean confirmed;
    private String confirmedBy;
    private LocalDateTime confirmedAt;
    private Boolean teamLeader;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String teamLeaderPassword;
    private String remark;
    private LocalDateTime lastAttendanceTime;
    private LocalDateTime lastSyncTime;
}
