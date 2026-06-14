package com.buildguard.repository;
import com.buildguard.entity.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByProjectId(Long projectId);
    void deleteByWorkerId(Long workerId);
}
