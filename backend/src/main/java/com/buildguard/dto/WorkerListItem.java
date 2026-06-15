package com.buildguard.dto;

import com.buildguard.entity.Worker;

import java.time.LocalDateTime;

public record WorkerListItem(
        Long id,
        String name,
        String personCode,
        String idCardNo,
        String phone,
        String gender,
        String workType,
        Long projectId,
        Long teamId,
        Long participantUnitId,
        String workerType,
        String status,
        String entryDate,
        String exitDate,
        String safetyEducationStatus,
        String accessStatus,
        String deviceSyncStatus,
        String qrToken,
        String realNameReportStatus,
        Boolean teamLeader,
        LocalDateTime lastAttendanceTime,
        LocalDateTime lastSyncTime,
        boolean hasAvatar
) {
    public static WorkerListItem from(Worker worker) {
        return new WorkerListItem(
                worker.getId(),
                worker.getName(),
                worker.getPersonCode(),
                worker.getIdCardNo(),
                worker.getPhone(),
                worker.getGender(),
                worker.getWorkType(),
                worker.getProjectId(),
                worker.getTeamId(),
                worker.getParticipantUnitId(),
                worker.getWorkerType(),
                worker.getStatus(),
                worker.getEntryDate(),
                worker.getExitDate(),
                worker.getSafetyEducationStatus(),
                worker.getAccessStatus(),
                worker.getDeviceSyncStatus(),
                worker.getQrToken(),
                worker.getRealNameReportStatus(),
                worker.getTeamLeader(),
                worker.getLastAttendanceTime(),
                worker.getLastSyncTime(),
                worker.getAvatarUrl() != null && !worker.getAvatarUrl().isBlank()
        );
    }
}
