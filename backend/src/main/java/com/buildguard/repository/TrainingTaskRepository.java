package com.buildguard.repository;

import com.buildguard.entity.TrainingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TrainingTaskRepository extends JpaRepository<TrainingTask, Long> {
    List<TrainingTask> findByProjectId(Long projectId);
    Optional<TrainingTask> findByTaskToken(String taskToken);
}
