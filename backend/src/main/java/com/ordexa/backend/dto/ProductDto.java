package com.ordexa.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public class ProductDto {

    public static class CreateProductRequest {
        @NotNull(message = "Company ID is required")
        private UUID companyId;

        @NotBlank(message = "SKU is required")
        private String sku;

        @NotBlank(message = "Product name is required")
        private String name;

        private String uom = "PCS";

        @DecimalMin(value = "0.0", inclusive = true, message = "Unit price must be non-negative")
        private BigDecimal unitPrice = BigDecimal.ZERO;

        public CreateProductRequest() {}

        public UUID getCompanyId() { return companyId; }
        public void setCompanyId(UUID companyId) { this.companyId = companyId; }

        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getUom() { return uom; }
        public void setUom(String uom) { this.uom = uom; }

        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    }

    public static class UpdateProductRequest {
        private String name;
        private String uom;
        private BigDecimal unitPrice;

        public UpdateProductRequest() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getUom() { return uom; }
        public void setUom(String uom) { this.uom = uom; }

        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    }
}
