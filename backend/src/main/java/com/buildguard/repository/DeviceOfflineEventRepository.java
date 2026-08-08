package com.buildguard.repository;

import com.buildguard.entity.DeviceOfflineEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DeviceOfflineEventRepository extends JpaRepository<DeviceOfflineEvent, Long> {
    List<DeviceOfflineEvent> findByProjectId(Long projectId);
    Optional<DeviceOfflineEvent> findFirstByDeviceTypeAndDeviceIdAndStatusOrderByCreatedAtDesc(String deviceType, Long deviceId, String status);
}
