package com.ordexa.backend.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "discrepancy_logs")
public class DiscrepancyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "shipment_id")
    private UUID shipmentId;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "discrepancy_type", nullable = false, length = 50)
    private String discrepancyType;

    @Column(name = "expected_value", nullable = false, columnDefinition = "TEXT")
    private String expectedValue;

    @Column(name = "observed_value", nullable = false, columnDefinition = "TEXT")
    private String observedValue;

    @Column(name = "detected_at_stage", nullable = false, length = 50)
    private String detectedAtStage;

    @Column(name = "resolution_status", nullable = false, length = 30)
    private String resolutionStatus = "OPEN";

    @Column(name = "exception_reason", columnDefinition = "TEXT")
    private String exceptionReason;

    @Column(name = "approved_by")
    private UUID approvedBy;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    public DiscrepancyEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getShipmentId() { return shipmentId; }
    public void setShipmentId(UUID shipmentId) { this.shipmentId = shipmentId; }
    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }
    public String getDiscrepancyType() { return discrepancyType; }
    public void setDiscrepancyType(String discrepancyType) { this.discrepancyType = discrepancyType; }
    public String getExpectedValue() { return expectedValue; }
    public void setExpectedValue(String expectedValue) { this.expectedValue = expectedValue; }
    public String getObservedValue() { return observedValue; }
    public void setObservedValue(String observedValue) { this.observedValue = observedValue; }
    public String getDetectedAtStage() { return detectedAtStage; }
    public void setDetectedAtStage(String detectedAtStage) { this.detectedAtStage = detectedAtStage; }
    public String getResolutionStatus() { return resolutionStatus; }
    public void setResolutionStatus(String resolutionStatus) { this.resolutionStatus = resolutionStatus; }
    public String getExceptionReason() { return exceptionReason; }
    public void setExceptionReason(String exceptionReason) { this.exceptionReason = exceptionReason; }
    public UUID getApprovedBy() { return approvedBy; }
    public void setApprovedBy(UUID approvedBy) { this.approvedBy = approvedBy; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
