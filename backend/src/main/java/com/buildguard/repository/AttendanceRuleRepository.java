package com.buildguard.repository;

import com.buildguard.entity.AttendanceRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttendanceRuleRepository extends JpaRepository<AttendanceRule, Long> {
    List<AttendanceRule> findByProjectIdOrderByIdDesc(Long projectId);
}
