package com.example.ordexa.data.repository

import com.example.ordexa.data.mock.MockData
import com.example.ordexa.domain.model.DiscrepancyLog
import com.example.ordexa.domain.model.Shipment
import com.example.ordexa.domain.model.ShipmentStatus
import com.example.ordexa.domain.model.Truck
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DispatchRepository {
    private val _shipments = MutableStateFlow(MockData.sampleShipments)
    val shipments: Flow<List<Shipment>> = _shipments.asStateFlow()

    private val _discrepancies = MutableStateFlow(MockData.sampleDiscrepancies)
    val discrepancies: Flow<List<DiscrepancyLog>> = _discrepancies.asStateFlow()

    val availableTrucks: List<Truck> = MockData.sampleTrucks

    fun assignTruckToShipment(shipmentId: String, truck: Truck): String? {
        val shipment = _shipments.value.find { it.id == shipmentId } ?: return "Shipment not found"

        // Rule Check: Route vs Destination validation
        if (!truck.routeName.lowercase().contains(shipment.destinationCity.lowercase())) {
            val discrepancy = DiscrepancyLog(
                id = "DISC-00${_discrepancies.value.size + 1}",
                shipmentId = shipmentId,
                orderId = shipment.assignedOrderIds.firstOrNull() ?: "N/A",
                discrepancyType = "DESTINATION_MISMATCH",
                expectedValue = "${shipment.destinationCity} Route",
                observedValue = "${truck.vehicleNumber} assigned to ${truck.routeName}",
                detectedAtStage = "Truck Assignment",
                resolutionStatus = "OPEN",
                exceptionReason = "Vehicle route ${truck.routeName} does not match destination ${shipment.destinationCity}"
            )
            _discrepancies.value = (listOf(discrepancy) + _discrepancies.value).toMutableList()

            // Update shipment to EXCEPTION_REVIEW
            _shipments.value = _shipments.value.map {
                if (it.id == shipmentId) it.copy(truck = truck, status = ShipmentStatus.EXCEPTION_REVIEW) else it
            }.toMutableList()

            return "Route Mismatch Detected! Truck ${truck.vehicleNumber} (${truck.routeName}) does not serve destination ${shipment.destinationCity}. Flagged for Exception Review."
        }

        // Standard Assignment
        _shipments.value = _shipments.value.map {
            if (it.id == shipmentId) it.copy(truck = truck, status = ShipmentStatus.ASSIGNED) else it
        }.toMutableList()

        return null // Success
    }

    fun approveDispatch(shipmentId: String, approverName: String): Boolean {
        _shipments.value = _shipments.value.map {
            if (it.id == shipmentId) {
                it.copy(status = ShipmentStatus.DISPATCH_APPROVED, approvedBy = approverName)
            } else it
        }.toMutableList()
        return true
    }

    fun resolveDiscrepancy(discrepancyId: String, approverName: String, reason: String) {
        _discrepancies.value = _discrepancies.value.map {
            if (it.id == discrepancyId) {
                it.copy(resolutionStatus = "RESOLVED", approvedBy = approverName, exceptionReason = reason)
            } else it
        }.toMutableList()
    }
}
