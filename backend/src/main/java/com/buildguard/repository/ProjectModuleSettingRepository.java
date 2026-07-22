package com.buildguard.repository;

import com.buildguard.entity.ProjectModuleSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface ProjectModuleSettingRepository extends JpaRepository<ProjectModuleSetting, Long> {
    List<ProjectModuleSetting> findByProjectId(Long projectId);
    Optional<ProjectModuleSetting> findByProjectIdAndModuleCode(Long projectId, String moduleCode);
}
