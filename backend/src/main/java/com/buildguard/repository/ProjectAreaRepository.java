package com.buildguard.repository;
import com.buildguard.entity.ProjectArea;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ProjectAreaRepository extends JpaRepository<ProjectArea, Long> {
    List<ProjectArea> findByProjectId(Long projectId);
}
