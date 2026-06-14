package com.buildguard.repository;
import com.buildguard.entity.EducationLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface EducationLedgerRepository extends JpaRepository<EducationLedger, Long> {
    List<EducationLedger> findByProjectId(Long projectId);
    List<EducationLedger> findByWorkerId(Long workerId);
    void deleteByWorkerId(Long workerId);
}
