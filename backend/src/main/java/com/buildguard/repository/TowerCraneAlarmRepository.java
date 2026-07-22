package com.buildguard.repository;

import com.buildguard.entity.TowerCraneAlarm;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface TowerCraneAlarmRepository extends JpaRepository<TowerCraneAlarm, Long> {
    List<TowerCraneAlarm> findByProjectId(Long projectId);
    List<TowerCraneAlarm> findByTowerCraneId(Long towerCraneId);
}
