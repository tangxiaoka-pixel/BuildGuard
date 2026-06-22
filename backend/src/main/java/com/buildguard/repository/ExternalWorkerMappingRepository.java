package com.buildguard.repository;

import com.buildguard.entity.ExternalWorkerMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ExternalWorkerMappingRepository extends JpaRepository<ExternalWorkerMapping, Long> {
    Optional<ExternalWorkerMapping> findByProjectIdAndExternalEmpId(Long projectId, String externalEmpId);
}
