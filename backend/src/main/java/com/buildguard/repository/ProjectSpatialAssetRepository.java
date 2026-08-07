package com.buildguard.repository;

import com.buildguard.entity.ProjectSpatialAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface ProjectSpatialAssetRepository extends JpaRepository<ProjectSpatialAsset, Long> {
    List<ProjectSpatialAsset> findByProjectIdAndAssetType(Long projectId, String assetType);
}
