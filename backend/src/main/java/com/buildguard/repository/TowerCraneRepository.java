package com.buildguard.repository;

import com.buildguard.entity.TowerCrane;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface TowerCraneRepository extends JpaRepository<TowerCrane, Long> {
    List<TowerCrane> findByProjectId(Long projectId);
    Optional<TowerCrane> findByIotDeviceId(Long iotDeviceId);
}
