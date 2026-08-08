package com.buildguard.repository;

import com.buildguard.entity.SystemDictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SystemDictionaryRepository extends JpaRepository<SystemDictionary, Long> {
    List<SystemDictionary> findByDictTypeOrderBySortOrderAscIdAsc(String dictType);
    Optional<SystemDictionary> findByDictTypeAndCode(String dictType, String code);
}
