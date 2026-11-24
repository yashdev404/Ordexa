package com.ordexa.backend.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "warehouses")
public class WarehouseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "factory_id", nullable = false)
    private UUID factoryId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    public WarehouseEntity() {}

    public WarehouseEntity(UUID factoryId, String name, String code) {
        this.factoryId = factoryId;
        this.name = name;
        this.code = code;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getFactoryId() { return factoryId; }
    public void setFactoryId(UUID factoryId) { this.factoryId = factoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
