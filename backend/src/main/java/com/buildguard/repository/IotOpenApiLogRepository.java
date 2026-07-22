package com.buildguard.repository;

import com.buildguard.entity.IotOpenApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface IotOpenApiLogRepository extends JpaRepository<IotOpenApiLog, Long> {
    List<IotOpenApiLog> findByProjectId(Long projectId);
}
