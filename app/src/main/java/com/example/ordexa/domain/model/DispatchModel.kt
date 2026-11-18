package com.example.ordexa.domain.model

enum class ShipmentStatus(val label: String) {
    DRAFT("Draft"),
    READY_FOR_ASSIGNMENT("Ready for Assignment"),
    ASSIGNED("Assigned"),
    LOADING("Loading"),
    LOADED("Loaded"),
    VERIFIED("Verified"),
    DISPATCH_APPROVED("Dispatch Approved"),
    DISPATCHED("Dispatched"),
    DELIVERED("Delivered"),
    ON_HOLD("On Hold"),
    EXCEPTION_REVIEW("Exception Review")
}

data class Truck(
    val id: String,
    val vehicleNumber: String,
    val driverName: String,
    val driverPhone: String,
    val routeName: String,
    val capacityKg: Double,
    val status: String = "Available"
)

data class Shipment(
    val id: String,
    val shipmentNumber: String,
    val factoryId: String,
    val destinationCity: String,
    val status: ShipmentStatus,
    val truck: Truck?,
    val assignedOrderIds: List<String>,
    val totalUnits: Int,
    val loadedUnits: Int,
    val createdBy: String,
    val approvedBy: String?
)

data class DiscrepancyLog(
    val id: String,
    val shipmentId: String,
    val orderId: String,
    val discrepancyType: String,
    val expectedValue: String,
    val observedValue: String,
    val detectedAtStage: String,
    val resolutionStatus: String = "OPEN",
    val exceptionReason: String? = null,
    val approvedBy: String? = null
)
