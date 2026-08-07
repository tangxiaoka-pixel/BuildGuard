package com.buildguard.repository;

import com.buildguard.entity.MonitorData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface MonitorDataRepository extends JpaRepository<MonitorData, Long> {
    List<MonitorData> findByProjectIdAndMonitorType(Long projectId, String monitorType);
    List<MonitorData> findByMonitorDeviceId(Long monitorDeviceId);
}
