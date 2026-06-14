package com.buildguard.service;

import com.buildguard.adapter.DeviceAdapter;
import com.buildguard.dto.AttendancePushRequest;
import com.buildguard.entity.*;
import com.buildguard.exception.BusinessException;
import com.buildguard.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessService {
    private final WorkerRepository workers;
    private final ProjectAreaRepository areas;
    private final WorkerAreaPermissionRepository permissions;
    private final DeviceRepository devices;
    private final AttendanceRecordRepository attendance;
    private final DeviceSyncLogRepository syncLogs;
    private final EducationLedgerRepository ledgers;
    private final DeviceAdapter deviceAdapter;

    @Transactional
    public List<DeviceSyncLog> syncWorker(Long workerId) {
        Worker worker = workers.findById(workerId).orElseThrow(() -> new BusinessException("人员不存在"));
        if (!"ON_SITE".equals(worker.getStatus())) throw new BusinessException("人员不在场，禁止下发");
        if (worker.getAvatarUrl() == null || worker.getAvatarUrl().isBlank()) throw new BusinessException("人员未采集人脸照片，禁止下发设备");
        refreshEducation(worker);
        if (!"PASSED".equals(worker.getSafetyEducationStatus())) throw new BusinessException("人员未通过安全教育，禁止下发设备");
        List<WorkerAreaPermission> workerPermissions = permissions.findByWorkerId(workerId).stream()
                .filter(this::isPermissionActive).toList();
        if (workerPermissions.isEmpty()) throw new BusinessException("人员未配置有效区域权限");
        List<DeviceSyncLog> result = workerPermissions.stream().flatMap(permission ->
                devices.findByAreaId(permission.getAreaId()).stream().map(device -> {
                    DeviceAdapter.SyncResult sync = deviceAdapter.pushWorkerToDevice(device, worker);
                    permission.setDeviceSyncStatus(sync.success() ? "SYNCED" : "SYNC_FAILED");
                    permission.setLastSyncTime(LocalDateTime.now());
                    permission.setLastSyncResult(sync.message());
                    permissions.save(permission);
                    return syncLogs.save(DeviceSyncLog.builder().projectId(worker.getProjectId()).areaId(device.getAreaId())
                            .deviceId(device.getId()).workerId(workerId).workerName(worker.getName()).deviceName(device.getDeviceName())
                            .actionType("ADD").syncStatus(sync.success() ? "SUCCESS" : "FAILED")
                            .errorMessage(sync.success() ? null : sync.message()).retryCount(0).syncedAt(LocalDateTime.now()).build());
                })).toList();
        worker.setDeviceSyncStatus(result.stream().allMatch(l -> "SUCCESS".equals(l.getSyncStatus())) ? "SYNCED" : "SYNC_FAILED");
        worker.setAccessStatus("SYNCED".equals(worker.getDeviceSyncStatus()) ? "ACCESS_ENABLED" : "ACCESS_DISABLED");
        worker.setLastSyncTime(LocalDateTime.now());
        workers.save(worker);
        return result;
    }

    @Transactional
    public AttendanceRecord pushAttendance(AttendancePushRequest request) {
        Device device = devices.findByDeviceNo(request.deviceNo()).orElseThrow(() -> new BusinessException("未找到设备"));
        Worker worker = request.personCode() != null ? workers.findByPersonCode(request.personCode()).orElse(null) : null;
        if (worker == null && request.idCardNo() != null) worker = workers.findByIdCardNo(request.idCardNo()).orElse(null);
        ProjectArea area = device.getAreaId() == null ? null : areas.findById(device.getAreaId()).orElse(null);
        String deny = validate(worker, area, request.score());
        AttendanceRecord record = attendance.save(AttendanceRecord.builder()
                .workerId(worker == null ? null : worker.getId()).workerName(worker == null ? request.personName() : worker.getName())
                .projectId(device.getProjectId()).deviceId(device.getId()).deviceName(device.getDeviceName())
                .areaId(device.getAreaId()).areaName(area == null ? "未绑定区域" : area.getAreaName())
                .direction(request.direction()).verifyType(request.verifyType()).score(request.score())
                .attendanceTime(request.attendanceTime() == null ? LocalDateTime.now() : request.attendanceTime())
                .accessResult(deny == null ? "ALLOW" : "DENY").denyReason(deny).reportStatus("PENDING").build());
        if (worker != null) { worker.setLastAttendanceTime(record.getAttendanceTime()); workers.save(worker); }
        return record;
    }

    private String validate(Worker worker, ProjectArea area, Double score) {
        if (worker == null) return "未找到人员";
        if (area == null) return "设备未绑定区域";
        if (!"ON_SITE".equals(worker.getStatus())) return "人员已退场或不在场";
        refreshEducation(worker);
        if (!"PASSED".equals(worker.getSafetyEducationStatus())) return "未通过安全教育";
        if (score != null && score < 80) return "识别分数过低";
        if (permissions.findByWorkerId(worker.getId()).stream().noneMatch(p -> area.getId().equals(p.getAreaId()) && isPermissionActive(p))) return "无区域权限";
        return null;
    }

    private boolean isPermissionActive(WorkerAreaPermission permission) {
        LocalDateTime now = LocalDateTime.now();
        return "ACTIVE".equals(permission.getPermissionStatus())
                && (permission.getValidStartTime() == null || !permission.getValidStartTime().isAfter(now))
                && (permission.getValidEndTime() == null || !permission.getValidEndTime().isBefore(now));
    }

    private void refreshEducation(Worker worker) {
        String today = java.time.LocalDate.now().toString();
        boolean passed = ledgers.findByWorkerId(worker.getId()).stream()
                .anyMatch(l -> Boolean.TRUE.equals(l.getPassed())
                        && (l.getValidStartDate() == null || l.getValidStartDate().compareTo(today) <= 0)
                        && (l.getValidEndDate() == null || l.getValidEndDate().compareTo(today) >= 0));
        worker.setSafetyEducationStatus(passed ? "PASSED" : "FAILED");
        workers.save(worker);
    }
}
