package com.buildguard.repository;

import com.buildguard.entity.ProjectRuntimeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProjectRuntimeConfigRepository extends JpaRepository<ProjectRuntimeConfig, Long> {
    Optional<ProjectRuntimeConfig> findByProjectId(Long projectId);
}
