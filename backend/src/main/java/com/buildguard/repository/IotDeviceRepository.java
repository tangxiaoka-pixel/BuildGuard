package com.buildguard.repository;

import com.buildguard.entity.IotDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface IotDeviceRepository extends JpaRepository<IotDevice, Long> {
    Optional<IotDevice> findByDeviceCode(String deviceCode);
    Optional<IotDevice> findFirstByDeviceCodeOrderByIdDesc(String deviceCode);
    List<IotDevice> findByProjectId(Long projectId);
    List<IotDevice> findByPendingBindTrue();
}
