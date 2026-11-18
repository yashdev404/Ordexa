package com.example.ordexa

import com.example.ordexa.data.repository.OrderRepository
import com.example.ordexa.domain.model.OrderStatus
import com.example.ordexa.domain.model.PurchaseOrder
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class OrderRepositoryTest {

    private lateinit var orderRepository: OrderRepository

    @Before
    fun setUp() {
        orderRepository = OrderRepository()
    }

    @Test
    fun `test duplicate PO registration is rejected`() {
        val existingPo = "NSC-PO-2026-0418"
        val duplicatePo = PurchaseOrder(
            id = "ORD-DUP-01",
            externalPoNumber = existingPo,
            customerName = "Northstar Components",
            orderDate = "2026-11-10",
            requestedDeliveryDate = "2026-11-14",
            status = OrderStatus.APPROVED,
            supplierName = "Apex Industrial",
            shipToAddress = "Baddi",
            billToAddress = "Chandigarh",
            lineItems = emptyList(),
            subtotal = 1000.0,
            gstAmount = 180.0,
            totalAmount = 1180.0
        )

        val isInserted = orderRepository.addOrder(duplicatePo)
        assertFalse("Duplicate PO should be rejected by uniqueness check", isInserted)
    }

    @Test
    fun `test status transition to RESERVED`() {
        val orderId = "ORD-NSC-0418"
        orderRepository.updateOrderStatus(orderId, OrderStatus.RESERVED)
        val updatedOrder = orderRepository.getOrderById(orderId)

        assertNotNull(updatedOrder)
        assertEquals(OrderStatus.RESERVED, updatedOrder?.status)
    }
}
