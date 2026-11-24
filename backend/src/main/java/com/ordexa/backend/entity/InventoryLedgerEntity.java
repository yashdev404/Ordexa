package com.ordexa.backend.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "inventory_ledger")
public class InventoryLedgerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "inventory_stock_id", nullable = false)
    private UUID inventoryStockId;

    @Column(name = "transaction_type", nullable = false, length = 30)
    private String transactionType; // RECEIPT, RESERVATION, PICK, DISPATCH, DAMAGE, ADJUSTMENT

    @Column(name = "reference_type", nullable = false, length = 30)
    private String referenceType; // GRN, ORDER, SHIPMENT, DAMAGE_REPORT, AUDIT

    @Column(name = "reference_id", nullable = false, length = 100)
    private String referenceId;

    @Column(name = "qty_in", nullable = false)
    private Integer qtyIn = 0;

    @Column(name = "qty_out", nullable = false)
    private Integer qtyOut = 0;

    @Column(name = "balance_after", nullable = false)
    private Integer balanceAfter;

    @Column(name = "performed_by", nullable = false)
    private UUID performedBy;

    @Column(name = "approved_by")
    private UUID approvedBy;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    public InventoryLedgerEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getInventoryStockId() { return inventoryStockId; }
    public void setInventoryStockId(UUID inventoryStockId) { this.inventoryStockId = inventoryStockId; }
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    public String getReferenceType() { return referenceType; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }
    public String getReferenceId() { return referenceId; }
    public void setReferenceId(String referenceId) { this.referenceId = referenceId; }
    public Integer getQtyIn() { return qtyIn; }
    public void setQtyIn(Integer qtyIn) { this.qtyIn = qtyIn; }
    public Integer getQtyOut() { return qtyOut; }
    public void setQtyOut(Integer qtyOut) { this.qtyOut = qtyOut; }
    public Integer getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(Integer balanceAfter) { this.balanceAfter = balanceAfter; }
    public UUID getPerformedBy() { return performedBy; }
    public void setPerformedBy(UUID performedBy) { this.performedBy = performedBy; }
    public UUID getApprovedBy() { return approvedBy; }
    public void setApprovedBy(UUID approvedBy) { this.approvedBy = approvedBy; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
