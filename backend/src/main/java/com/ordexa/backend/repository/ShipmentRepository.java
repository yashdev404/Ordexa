package com.ordexa.backend.repository;

import com.ordexa.backend.entity.ShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipmentRepository extends JpaRepository<ShipmentEntity, UUID> {
    List<ShipmentEntity> findByFactoryId(UUID factoryId);
    Optional<ShipmentEntity> findByShipmentNumber(String shipmentNumber);
}
