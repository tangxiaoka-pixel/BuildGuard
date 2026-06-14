package com.buildguard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity @Table(name = "work_team")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Team extends BaseEntity {
    private Long projectId;
    private Long participantUnitId;
    private String teamName;
    private String leaderName;
    private String workType;
    private String status;
}
