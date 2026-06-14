package com.buildguard.repository;

import com.buildguard.entity.WorkTypeDictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WorkTypeDictionaryRepository extends JpaRepository<WorkTypeDictionary, Long> {
    List<WorkTypeDictionary> findAllByOrderBySortOrderAscIdAsc();
    Optional<WorkTypeDictionary> findByCode(String code);
    Optional<WorkTypeDictionary> findByName(String name);
}
