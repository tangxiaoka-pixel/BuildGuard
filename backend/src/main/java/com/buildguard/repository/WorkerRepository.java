package com.buildguard.repository;
import com.buildguard.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Optional<Worker> findByPersonCode(String personCode);
    Optional<Worker> findByIdCardNo(String idCardNo);
    Optional<Worker> findByQrToken(String qrToken);
    List<Worker> findByProjectId(Long projectId);
}
