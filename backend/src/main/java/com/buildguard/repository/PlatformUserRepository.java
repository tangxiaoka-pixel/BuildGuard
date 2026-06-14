package com.buildguard.repository;

import com.buildguard.entity.PlatformUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface PlatformUserRepository extends JpaRepository<PlatformUser, Long> {
    Optional<PlatformUser> findByPhone(String phone);
    Optional<PlatformUser> findByUsername(String username);
    List<PlatformUser> findByTenantId(Long tenantId);
    List<PlatformUser> findByCompanyId(Long companyId);
    List<PlatformUser> findByCompanyIdsContaining(Long companyId);
}
