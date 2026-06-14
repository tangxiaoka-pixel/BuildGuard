package com.buildguard.repository;
import com.buildguard.entity.EducationCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface EducationCourseRepository extends JpaRepository<EducationCourse, Long> {
    List<EducationCourse> findByProjectId(Long projectId);
}
