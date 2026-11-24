package com.ordexa.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class DispatchDto {

    public static class TruckAssignRequest {
        @NotNull
        private UUID shipmentId;

        @NotNull
        private UUID truckId;

        public TruckAssignRequest() {}

        public UUID getShipmentId() { return shipmentId; }
        public void setShipmentId(UUID shipmentId) { this.shipmentId = shipmentId; }
        public UUID getTruckId() { return truckId; }
        public void setTruckId(UUID truckId) { this.truckId = truckId; }
    }

    public static class BarcodeVerifyRequest {
        @NotNull
        private UUID shipmentId;

        @NotBlank
        private String scannedSku;

        public BarcodeVerifyRequest() {}

        public UUID getShipmentId() { return shipmentId; }
        public void setShipmentId(UUID shipmentId) { this.shipmentId = shipmentId; }
        public String getScannedSku() { return scannedSku; }
        public void setScannedSku(String scannedSku) { this.scannedSku = scannedSku; }
    }
}
