package com.example.ordexa.domain.model

enum class TransactionType(val label: String) {
    RECEIPT("Receipt"),
    RESERVATION("Reservation"),
    PICK("Pick"),
    DISPATCH("Dispatch"),
    DAMAGE("Damage"),
    ADJUSTMENT("Adjustment")
}

data class InventoryStock(
    val id: String,
    val warehouseId: String,
    val warehouseName: String,
    val sku: String,
    val productName: String,
    val physicalQty: Int,
    val reservedQty: Int,
    val unitOfMeasure: String = "PCS"
) {
    val availableQty: Int get() = physicalQty - reservedQty
}

data class InventoryLedger(
    val id: String,
    val date: String,
    val factory: String,
    val warehouse: String,
    val sku: String,
    val productName: String,
    val transactionType: TransactionType,
    val reference: String,
    val qtyIn: Int,
    val qtyOut: Int,
    val balanceAfter: Int,
    val performedBy: String,
    val approvedBy: String?,
    val notes: String
)
