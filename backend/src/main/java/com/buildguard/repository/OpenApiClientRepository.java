package com.buildguard.repository;

import com.buildguard.entity.OpenApiClient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OpenApiClientRepository extends JpaRepository<OpenApiClient, Long> {
    Optional<OpenApiClient> findByApiKey(String apiKey);
}
