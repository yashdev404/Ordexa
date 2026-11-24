package com.ordexa.backend.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "inventory_stock")
public class InventoryStockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "warehouse_id", nullable = false)
    private UUID warehouseId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(nullable = false, length = 50)
    private String sku;

    @Column(name = "physical_qty", nullable = false)
    private Integer physicalQty = 0;

    @Column(name = "reserved_qty", nullable = false)
    private Integer reservedQty = 0;

    @Version
    @Column(nullable = false)
    private Long version = 0L;

    public InventoryStockEntity() {}

    public Integer getAvailableQty() {
        return physicalQty - reservedQty;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getWarehouseId() { return warehouseId; }
    public void setWarehouseId(UUID warehouseId) { this.warehouseId = warehouseId; }
    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public Integer getPhysicalQty() { return physicalQty; }
    public void setPhysicalQty(Integer physicalQty) { this.physicalQty = physicalQty; }
    public Integer getReservedQty() { return reservedQty; }
    public void setReservedQty(Integer reservedQty) { this.reservedQty = reservedQty; }
    public Long getVersion() { return version; }
}
