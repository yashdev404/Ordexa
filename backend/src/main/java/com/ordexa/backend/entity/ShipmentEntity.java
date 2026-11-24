package com.ordexa.backend.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "shipments")
public class ShipmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "factory_id", nullable = false)
    private UUID factoryId;

    @Column(name = "truck_id")
    private UUID truckId;

    @Column(name = "shipment_number", nullable = false, unique = true, length = 50)
    private String shipmentNumber;

    @Column(name = "destination_city", nullable = false, length = 100)
    private String destinationCity;

    @Column(nullable = false, length = 30)
    private String status = "DRAFT";

    @Column(name = "total_units", nullable = false)
    private Integer totalUnits = 0;

    @Column(name = "loaded_units", nullable = false)
    private Integer loadedUnits = 0;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "approved_by")
    private UUID approvedBy;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    public ShipmentEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getFactoryId() { return factoryId; }
    public void setFactoryId(UUID factoryId) { this.factoryId = factoryId; }
    public UUID getTruckId() { return truckId; }
    public void setTruckId(UUID truckId) { this.truckId = truckId; }
    public String getShipmentNumber() { return shipmentNumber; }
    public void setShipmentNumber(String shipmentNumber) { this.shipmentNumber = shipmentNumber; }
    public String getDestinationCity() { return destinationCity; }
    public void setDestinationCity(String destinationCity) { this.destinationCity = destinationCity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getTotalUnits() { return totalUnits; }
    public void setTotalUnits(Integer totalUnits) { this.totalUnits = totalUnits; }
    public Integer getLoadedUnits() { return loadedUnits; }
    public void setLoadedUnits(Integer loadedUnits) { this.loadedUnits = loadedUnits; }
    public UUID getCreatedBy() { return createdBy; }
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }
    public UUID getApprovedBy() { return approvedBy; }
    public void setApprovedBy(UUID approvedBy) { this.approvedBy = approvedBy; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
