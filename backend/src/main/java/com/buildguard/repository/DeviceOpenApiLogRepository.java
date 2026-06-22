package com.buildguard.repository;

import com.buildguard.entity.DeviceOpenApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DeviceOpenApiLogRepository extends JpaRepository<DeviceOpenApiLog, Long> {
    List<DeviceOpenApiLog> findByDeviceNo(String deviceNo);
    List<DeviceOpenApiLog> findByInterfaceName(String interfaceName);
}
