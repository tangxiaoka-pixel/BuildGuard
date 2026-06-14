package com.buildguard.repository;
import com.buildguard.entity.WorkerAreaPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface WorkerAreaPermissionRepository extends JpaRepository<WorkerAreaPermission, Long> {
    List<WorkerAreaPermission> findByWorkerId(Long workerId);
    void deleteByWorkerId(Long workerId);
    boolean existsByWorkerIdAndAreaIdAndPermissionStatus(Long workerId, Long areaId, String status);
}
