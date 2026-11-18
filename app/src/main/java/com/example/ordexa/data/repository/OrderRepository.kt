package com.example.ordexa.data.repository

import com.example.ordexa.data.mock.MockData
import com.example.ordexa.domain.model.OrderStatus
import com.example.ordexa.domain.model.PurchaseOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrderRepository {
    private val _orders = MutableStateFlow(MockData.sampleOrders)
    val orders: Flow<List<PurchaseOrder>> = _orders.asStateFlow()

    fun getOrderById(id: String): PurchaseOrder? {
        return _orders.value.find { it.id == id || it.externalPoNumber == id }
    }

    fun addOrder(order: PurchaseOrder): Boolean {
        if (_orders.value.any { it.externalPoNumber == order.externalPoNumber }) {
            return false // Duplicate PO check
        }
        _orders.value = (_orders.value + order).toMutableList()
        return true
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        _orders.value = _orders.value.map {
            if (it.id == orderId) it.copy(status = newStatus) else it
        }.toMutableList()
    }
}
