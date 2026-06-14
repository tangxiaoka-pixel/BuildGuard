package com.buildguard.repository;
import com.buildguard.entity.ParticipantUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ParticipantUnitRepository extends JpaRepository<ParticipantUnit, Long> {
    List<ParticipantUnit> findByProjectId(Long projectId);
}
