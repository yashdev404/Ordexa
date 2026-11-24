package com.ordexa.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class OrderDto {

    public static class OrderItemDto {
        @NotNull
        private UUID productId;

        @NotBlank
        private String sku;

        private String description;

        private String uom = "PCS";

        @NotNull
        private Integer quantity;

        @NotNull
        private BigDecimal unitPrice;

        public OrderItemDto() {}

        public UUID getProductId() { return productId; }
        public void setProductId(UUID productId) { this.productId = productId; }
        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getUom() { return uom; }
        public void setUom(String uom) { this.uom = uom; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    }

    public static class OrderIngestRequest {
        @NotNull
        private UUID companyId;

        @NotNull
        private UUID factoryId;

        @NotNull
        private UUID customerId;

        @NotBlank
        private String externalPoNumber;

        @NotNull
        private LocalDate orderDate;

        private LocalDate requestedDeliveryDate;

        private String priority = "Normal";

        private String supplierName;

        private String shipToAddress;

        private String billToAddress;

        private String deliveryInstructions;

        @NotEmpty
        private List<OrderItemDto> lineItems;

        public OrderIngestRequest() {}

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
        public String getSupplierName() { return supplierName; }
        public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
        public String getShipToAddress() { return shipToAddress; }
        public void setShipToAddress(String shipToAddress) { this.shipToAddress = shipToAddress; }
        public String getBillToAddress() { return billToAddress; }
        public void setBillToAddress(String billToAddress) { this.billToAddress = billToAddress; }
        public String getDeliveryInstructions() { return deliveryInstructions; }
        public void setDeliveryInstructions(String deliveryInstructions) { this.deliveryInstructions = deliveryInstructions; }
        public List<OrderItemDto> getLineItems() { return lineItems; }
        public void setLineItems(List<OrderItemDto> lineItems) { this.lineItems = lineItems; }
    }
}
