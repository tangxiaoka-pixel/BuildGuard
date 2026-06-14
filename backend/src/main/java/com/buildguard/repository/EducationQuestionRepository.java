package com.buildguard.repository;
import com.buildguard.entity.EducationQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface EducationQuestionRepository extends JpaRepository<EducationQuestion, Long> {
    List<EducationQuestion> findByCourseId(Long courseId);
    List<EducationQuestion> findByProjectId(Long projectId);
}
