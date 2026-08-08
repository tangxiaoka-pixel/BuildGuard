package com.buildguard.repository;

import com.buildguard.entity.FileResource;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FileResourceRepository extends JpaRepository<FileResource, Long> {
    List<FileResource> findByProjectIdAndDeletedFalseOrderByCreatedAtDesc(Long projectId);
}
