package com.buildguard.service;

import com.buildguard.entity.Device;
import com.buildguard.entity.DeviceOfflineEvent;
import com.buildguard.entity.IotDevice;
import com.buildguard.repository.DeviceOfflineEventRepository;
import com.buildguard.repository.DeviceRepository;
import com.buildguard.repository.IotDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeviceStatusScheduler {
    private static final int OFFLINE_MINUTES = 10;
    private final DeviceRepository devices;
    private final IotDeviceRepository iotDevices;
    private final DeviceOfflineEventRepository events;

    @Scheduled(fixedDelay = 60_000)
    public void refreshOfflineStatus() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(OFFLINE_MINUTES);
        devices.findAll().forEach(device -> refreshAttendanceDevice(device, threshold));
        iotDevices.findAll().forEach(device -> refreshIotDevice(device, threshold));
    }

    private void refreshAttendanceDevice(Device device, LocalDateTime threshold) {
        LocalDateTime alive = device.getLastHeartbeatTime() == null ? device.getLastSigninTime() : device.getLastHeartbeatTime();
        boolean offline = alive != null && alive.isBefore(threshold) && !"DISABLED".equals(device.getStatus());
        if (offline) {
            if (!"OFFLINE".equals(device.getStatus())) { device.setStatus("OFFLINE"); devices.save(device); }
            createIfNeeded("ATTENDANCE", device.getId(), device.getProjectId(), device.getDeviceName(), device.getDeviceNo());
        } else if ("OFFLINE".equals(device.getStatus()) && alive != null) {
            device.setStatus("ONLINE"); devices.save(device); recover("ATTENDANCE", device.getId());
        }
    }

    private void refreshIotDevice(IotDevice device, LocalDateTime threshold) {
        boolean offline = device.getLastMessageTime() != null && device.getLastMessageTime().isBefore(threshold) && !"DISABLED".equals(device.getStatus());
        if (offline) {
            if (!"OFFLINE".equals(device.getStatus())) { device.setStatus("OFFLINE"); iotDevices.save(device); }
            createIfNeeded("IOT", device.getId(), device.getProjectId(), device.getDeviceName(), device.getDeviceCode());
        } else if ("OFFLINE".equals(device.getStatus()) && device.getLastMessageTime() != null) {
            device.setStatus("ONLINE"); iotDevices.save(device); recover("IOT", device.getId());
        }
    }

    private void createIfNeeded(String type, Long id, Long projectId, String name, String code) {
        if (events.findFirstByDeviceTypeAndDeviceIdAndStatusOrderByCreatedAtDesc(type, id, "UNHANDLED").isPresent()) return;
        DeviceOfflineEvent event = DeviceOfflineEvent.builder().projectId(projectId).deviceId(id).deviceType(type).deviceName(name).deviceCode(code).status("UNHANDLED").offlineAt(LocalDateTime.now()).build();
        event.setTenantId(1L);
        events.save(event);
    }

    private void recover(String type, Long id) {
        events.findFirstByDeviceTypeAndDeviceIdAndStatusOrderByCreatedAtDesc(type, id, "UNHANDLED").ifPresent(event -> {
            event.setStatus("RECOVERED"); event.setRecoveredAt(LocalDateTime.now()); events.save(event);
        });
    }
}
