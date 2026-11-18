package com.example.ordexa.domain.model

enum class OrderStatus(val label: String) {
    DRAFT("Draft"),
    VERIFIED("Verified"),
    APPROVED("Approved"),
    RESERVED("Reserved"),
    PARTIALLY_DISPATCHED("Partially Dispatched"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled")
}

data class OrderItem(
    val id: String,
    val sku: String,
    val description: String,
    val uom: String = "PCS",
    val quantity: Int,
    val unitPrice: Double,
    val totalAmount: Double
)

data class PurchaseOrder(
    val id: String,
    val externalPoNumber: String,
    val customerName: String,
    val orderDate: String,
    val requestedDeliveryDate: String,
    val priority: String = "Normal",
    val status: OrderStatus,
    val supplierName: String,
    val shipToAddress: String,
    val billToAddress: String,
    val lineItems: List<OrderItem>,
    val subtotal: Double,
    val gstAmount: Double,
    val totalAmount: Double,
    val deliveryInstructions: String = ""
)
