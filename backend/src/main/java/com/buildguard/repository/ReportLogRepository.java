package com.buildguard.repository;
import com.buildguard.entity.ReportLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ReportLogRepository extends JpaRepository<ReportLog, Long> {
    List<ReportLog> findByTenantId(Long tenantId);
}
