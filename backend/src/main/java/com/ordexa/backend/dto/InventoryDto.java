package com.ordexa.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class InventoryDto {

    public static class GrnReceiptRequest {
        @NotNull
        private UUID warehouseId;

        @NotNull
        private UUID productId;

        @NotBlank
        private String sku;

        @NotNull
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;

        @NotBlank
        private String grnReference;

        @NotNull
        private UUID performedBy;

        @NotNull
        private UUID approvedBy;

        public GrnReceiptRequest() {}

        public UUID getWarehouseId() { return warehouseId; }
        public void setWarehouseId(UUID warehouseId) { this.warehouseId = warehouseId; }
        public UUID getProductId() { return productId; }
        public void setProductId(UUID productId) { this.productId = productId; }
        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public String getGrnReference() { return grnReference; }
        public void setGrnReference(String grnReference) { this.grnReference = grnReference; }
        public UUID getPerformedBy() { return performedBy; }
        public void setPerformedBy(UUID performedBy) { this.performedBy = performedBy; }
        public UUID getApprovedBy() { return approvedBy; }
        public void setApprovedBy(UUID approvedBy) { this.approvedBy = approvedBy; }
    }

    public static class DamageReportRequest {
        @NotNull
        private UUID warehouseId;

        @NotBlank
        private String sku;

        @NotNull
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;

        @NotBlank
        private String damageReference;

        @NotNull
        private UUID performedBy;

        private UUID approvedBy;

        private String notes;

        public DamageReportRequest() {}

        public UUID getWarehouseId() { return warehouseId; }
        public void setWarehouseId(UUID warehouseId) { this.warehouseId = warehouseId; }
        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public String getDamageReference() { return damageReference; }
        public void setDamageReference(String damageReference) { this.damageReference = damageReference; }
        public UUID getPerformedBy() { return performedBy; }
        public void setPerformedBy(UUID performedBy) { this.performedBy = performedBy; }
        public UUID getApprovedBy() { return approvedBy; }
        public void setApprovedBy(UUID approvedBy) { this.approvedBy = approvedBy; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
}
