package com.example.ordexa.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shipments_table")
data class ShipmentEntity(
    @PrimaryKey val id: String,
    val shipmentNumber: String,
    val factoryId: String,
    val destinationCity: String,
    val status: String,
    val truckVehicleNumber: String?,
    val truckDriverName: String?,
    val truckDriverPhone: String?,
    val truckRouteName: String?,
    val totalUnits: Int,
    val loadedUnits: Int,
    val createdBy: String,
    val approvedBy: String?
)

@Entity(tableName = "discrepancies_table")
data class DiscrepancyEntity(
    @PrimaryKey val id: String,
    val shipmentId: String,
    val orderId: String,
    val discrepancyType: String,
    val expectedValue: String,
    val observedValue: String,
    val detectedAtStage: String,
    val resolutionStatus: String,
    val exceptionReason: String?,
    val approvedBy: String?
)
