package com.buildguard.repository;
import com.buildguard.entity.EducationExam;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface EducationExamRepository extends JpaRepository<EducationExam, Long> {
    List<EducationExam> findByProjectId(Long projectId);
    void deleteByWorkerId(Long workerId);
}
