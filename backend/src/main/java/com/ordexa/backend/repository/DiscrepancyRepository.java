package com.ordexa.backend.repository;

import com.ordexa.backend.entity.DiscrepancyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DiscrepancyRepository extends JpaRepository<DiscrepancyEntity, UUID> {
    List<DiscrepancyEntity> findByResolutionStatus(String resolutionStatus);
}
