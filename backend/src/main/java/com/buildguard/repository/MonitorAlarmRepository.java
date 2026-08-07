package com.buildguard.repository;

import com.buildguard.entity.MonitorAlarm;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface MonitorAlarmRepository extends JpaRepository<MonitorAlarm, Long> {
    List<MonitorAlarm> findByProjectIdAndMonitorType(Long projectId, String monitorType);
    List<MonitorAlarm> findByMonitorDeviceId(Long monitorDeviceId);
}
