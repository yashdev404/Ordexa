package com.example.ordexa.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders_table")
data class OrderEntity(
    @PrimaryKey val id: String,
    val externalPoNumber: String,
    val customerName: String,
    val orderDate: String,
    val requestedDeliveryDate: String,
    val priority: String,
    val status: String,
    val supplierName: String,
    val shipToAddress: String,
    val billToAddress: String,
    val subtotal: Double,
    val gstAmount: Double,
    val totalAmount: Double,
    val deliveryInstructions: String
)

@Entity(tableName = "order_items_table")
data class OrderItemEntity(
    @PrimaryKey val id: String,
    val orderId: String,
    val sku: String,
    val description: String,
    val uom: String,
    val quantity: Int,
    val unitPrice: Double,
    val totalAmount: Double
)
