package com.ordexa.backend.repository;

import com.ordexa.backend.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    List<ProductEntity> findByCompanyId(UUID companyId);
    Optional<ProductEntity> findByCompanyIdAndSku(UUID companyId, String sku);
    boolean existsByCompanyIdAndSku(UUID companyId, String sku);
}
