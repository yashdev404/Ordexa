package com.ordexa.backend.repository;

import com.ordexa.backend.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    List<OrderEntity> findByFactoryId(UUID factoryId);
    Optional<OrderEntity> findByCompanyIdAndCustomerIdAndExternalPoNumber(UUID companyId, UUID customerId, String externalPoNumber);
    boolean existsByCompanyIdAndCustomerIdAndExternalPoNumber(UUID companyId, UUID customerId, String externalPoNumber);
}
