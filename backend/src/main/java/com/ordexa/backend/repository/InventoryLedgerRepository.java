package com.ordexa.backend.repository;

import com.ordexa.backend.entity.InventoryLedgerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryLedgerRepository extends JpaRepository<InventoryLedgerEntity, UUID> {
    List<InventoryLedgerEntity> findByInventoryStockIdOrderByCreatedAtDesc(UUID inventoryStockId);
}
