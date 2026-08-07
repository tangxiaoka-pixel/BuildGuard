package com.buildguard.repository;

import com.buildguard.entity.MonitorDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface MonitorDeviceRepository extends JpaRepository<MonitorDevice, Long> {
    List<MonitorDevice> findByProjectIdAndMonitorType(Long projectId, String monitorType);
    Optional<MonitorDevice> findByIotDeviceIdAndMonitorType(Long iotDeviceId, String monitorType);
}
