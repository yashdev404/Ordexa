package com.example.ordexa.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ordexa.data.repository.OrderRepository
import com.example.ordexa.domain.model.OrderItem
import com.example.ordexa.domain.model.OrderStatus
import java.util.Locale

import com.example.ordexa.ui.components.OrdexaTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    orderId: String,
    orderRepository: OrderRepository,
    onNavigateBack: () -> Unit,
    onNavigateToAiAssistant: () -> Unit,
    onLogoutConfirmed: () -> Unit
) {
    val context = LocalContext.current
    var order by remember { mutableStateOf(orderRepository.getOrderById(orderId)) }

    if (order == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Order Not Found ($orderId)")
        }
        return
    }

    val currentOrder = order!!

    Scaffold(
        topBar = {
            OrdexaTopAppBar(
                title = "PO ${currentOrder.externalPoNumber}",
                onNavigateBack = onNavigateBack,
                onNavigateToAiAssistant = onNavigateToAiAssistant,
                onLogoutConfirmed = onLogoutConfirmed
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (currentOrder.status == OrderStatus.APPROVED) {
                        Button(
                            onClick = {
                                orderRepository.updateOrderStatus(currentOrder.id, OrderStatus.RESERVED)
                                order = orderRepository.getOrderById(currentOrder.id)
                                Toast.makeText(context, "Stock Reserved for ${currentOrder.externalPoNumber}", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F4C75))
                        ) {
                            Icon(Icons.Default.Inventory, contentDescription = null, tint = Color(0xFFF5B027))
                            Spacer(Modifier.width(8.dp))
                            Text("Reserve Inventory Stock", fontWeight = FontWeight.Bold)
                        }
                    } else if (currentOrder.status == OrderStatus.RESERVED) {
                        OutlinedButton(
                            onClick = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            enabled = false
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981))
                            Spacer(Modifier.width(8.dp))
                            Text("Stock Already Reserved", fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                        }
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF1F5F9))
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Info Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(currentOrder.externalPoNumber, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F4C75))
                            OrderStatusChip(status = currentOrder.status)
                        }

                        Text("Buyer: ${currentOrder.customerName}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color(0xFF1B262C))
                        Text("Supplier: ${currentOrder.supplierName}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))

                        HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 4.dp))

                        Text("Ship To: ${currentOrder.shipToAddress}", style = MaterialTheme.typography.bodySmall)
                        Text("Bill To: ${currentOrder.billToAddress}", style = MaterialTheme.typography.bodySmall)

                        Text("Order Date: ${currentOrder.orderDate} | Requested Due: ${currentOrder.requestedDeliveryDate}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF0F4C75), fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Line Items Section Header
            item {
                Text("Order Line Items Breakdown", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF1B262C))
            }

            items(currentOrder.lineItems) { item ->
                OrderItemRow(item = item)
            }

            // Delivery Instructions & Financial Summary
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Delivery Instructions", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color(0xFF0F4C75))
                        Text(
                            text = currentOrder.deliveryInstructions.ifEmpty { "Standard factory receiving protocol." },
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF475569)
                        )

                        HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 8.dp))

                        FinancialRow("Subtotal", currentOrder.subtotal)
                        FinancialRow("GST (18%)", currentOrder.gstAmount)
                        FinancialRow("Total Amount", currentOrder.totalAmount, isBold = true)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItemRow(item: OrderItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.sku, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F4C75))
                Text(item.description, style = MaterialTheme.typography.bodySmall, color = Color(0xFF1B262C))
                Text("Qty: ${item.quantity} ${item.uom} @ ₹${String.format(Locale.US, "%.2f", item.unitPrice)}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
            }
            Text("₹${String.format(Locale.US, "%.2f", item.totalAmount)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF1B262C))
        }
    }
}

@Composable
fun FinancialRow(label: String, amount: Double, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = if (isBold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal, color = if (isBold) Color(0xFF1B262C) else Color(0xFF64748B))
        Text("₹${String.format(Locale.US, "%.2f", amount)}", style = if (isBold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium, fontWeight = if (isBold) FontWeight.ExtraBold else FontWeight.Normal, color = if (isBold) Color(0xFF0F4C75) else Color(0xFF1B262C))
    }
}
