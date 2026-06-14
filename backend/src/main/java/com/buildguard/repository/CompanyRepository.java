package com.buildguard.repository;
import com.buildguard.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByTenantId(Long tenantId);
}
