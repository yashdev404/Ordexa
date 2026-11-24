package com.ordexa.backend.repository;

import com.ordexa.backend.entity.InventoryStockEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryStockRepository extends JpaRepository<InventoryStockEntity, UUID> {

    List<InventoryStockEntity> findByWarehouseId(UUID warehouseId);

    Optional<InventoryStockEntity> findByWarehouseIdAndSku(UUID warehouseId, String sku);

    // Pessimistic Lock for Stock Allocations & Ledger Updates
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM InventoryStockEntity s WHERE s.warehouseId = :warehouseId AND s.sku = :sku")
    Optional<InventoryStockEntity> findByWarehouseIdAndSkuForUpdate(UUID warehouseId, String sku);
}
