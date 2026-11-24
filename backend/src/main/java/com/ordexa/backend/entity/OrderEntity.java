package com.ordexa.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "factory_id", nullable = false)
    private UUID factoryId;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "external_po_number", nullable = false, length = 100)
    private String externalPoNumber;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "requested_delivery_date")
    private LocalDate requestedDeliveryDate;

    @Column(length = 20)
    private String priority = "Normal";

    @Column(nullable = false, length = 30)
    private String status = "DRAFT"; // DRAFT, VERIFIED, APPROVED, RESERVED, PARTIALLY_DISPATCHED, COMPLETED, CANCELLED

    @Column(name = "supplier_name", length = 150)
    private String supplierName;

    @Column(name = "ship_to_address", columnDefinition = "TEXT")
    private String shipToAddress;

    @Column(name = "bill_to_address", columnDefinition = "TEXT")
    private String billToAddress;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "gst_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal gstAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "delivery_instructions", columnDefinition = "TEXT")
    private String deliveryInstructions;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> lineItems = new ArrayList<>();

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    public OrderEntity() {}

    public void addLineItem(OrderItemEntity item) {
        lineItems.add(item);
        item.setOrder(this);
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getCompanyId() { return companyId; }
    public void setCompanyId(UUID companyId) { this.companyId = companyId; }
    public UUID getFactoryId() { return factoryId; }
    public void setFactoryId(UUID factoryId) { this.factoryId = factoryId; }
    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }
    public String getExternalPoNumber() { return externalPoNumber; }
    public void setExternalPoNumber(String externalPoNumber) { this.externalPoNumber = externalPoNumber; }
    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
    public LocalDate getRequestedDeliveryDate() { return requestedDeliveryDate; }
    public void setRequestedDeliveryDate(LocalDate requestedDeliveryDate) { this.requestedDeliveryDate = requestedDeliveryDate; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public String getShipToAddress() { return shipToAddress; }
    public void setShipToAddress(String shipToAddress) { this.shipToAddress = shipToAddress; }
    public String getBillToAddress() { return billToAddress; }
    public void setBillToAddress(String billToAddress) { this.billToAddress = billToAddress; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getGstAmount() { return gstAmount; }
    public void setGstAmount(BigDecimal gstAmount) { this.gstAmount = gstAmount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getDeliveryInstructions() { return deliveryInstructions; }
    public void setDeliveryInstructions(String deliveryInstructions) { this.deliveryInstructions = deliveryInstructions; }
    public List<OrderItemEntity> getLineItems() { return lineItems; }
    public void setLineItems(List<OrderItemEntity> lineItems) { this.lineItems = lineItems; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
