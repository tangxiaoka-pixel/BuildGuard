package com.buildguard.repository;

import com.buildguard.entity.ScopeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScopeRoleRepository extends JpaRepository<ScopeRole, Long> {
    List<ScopeRole> findByScopeTypeAndScopeId(String scopeType, Long scopeId);
}
