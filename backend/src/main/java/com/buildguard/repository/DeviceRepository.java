package com.buildguard.repository;
import com.buildguard.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByAreaId(Long areaId);
    List<Device> findByProjectId(Long projectId);
    List<Device> findByPendingConfirmTrue();
    Optional<Device> findByDeviceNo(String deviceNo);
}
