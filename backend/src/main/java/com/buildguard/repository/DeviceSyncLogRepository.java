package com.buildguard.repository;
import com.buildguard.entity.DeviceSyncLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface DeviceSyncLogRepository extends JpaRepository<DeviceSyncLog, Long> {
    List<DeviceSyncLog> findByProjectId(Long projectId);
    void deleteByWorkerId(Long workerId);
}
