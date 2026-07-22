package com.buildguard.repository;

import com.buildguard.entity.TowerCraneData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface TowerCraneDataRepository extends JpaRepository<TowerCraneData, Long> {
    List<TowerCraneData> findByProjectId(Long projectId);
    List<TowerCraneData> findByTowerCraneId(Long towerCraneId);
}
