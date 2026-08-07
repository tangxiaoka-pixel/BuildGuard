package com.buildguard.repository;

import com.buildguard.entity.ProjectSpatialPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface ProjectSpatialPointRepository extends JpaRepository<ProjectSpatialPoint, Long> {
    List<ProjectSpatialPoint> findByProjectId(Long projectId);
    List<ProjectSpatialPoint> findByAssetId(Long assetId);
}
