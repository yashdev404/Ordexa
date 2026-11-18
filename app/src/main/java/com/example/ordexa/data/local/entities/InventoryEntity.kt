package com.example.ordexa.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventory_stock_table")
data class InventoryStockEntity(
    @PrimaryKey val id: String,
    val warehouseId: String,
    val warehouseName: String,
    val sku: String,
    val productName: String,
    val physicalQty: Int,
    val reservedQty: Int,
    val unitOfMeasure: String = "PCS"
)

@Entity(tableName = "inventory_ledger_table")
data class InventoryLedgerEntity(
    @PrimaryKey val id: String,
    val date: String,
    val factory: String,
    val warehouse: String,
    val sku: String,
    val productName: String,
    val transactionType: String,
    val reference: String,
    val qtyIn: Int,
    val qtyOut: Int,
    val balanceAfter: Int,
    val performedBy: String,
    val approvedBy: String?,
    val notes: String
)
