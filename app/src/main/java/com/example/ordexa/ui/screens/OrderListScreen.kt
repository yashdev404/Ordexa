package com.example.ordexa.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ordexa.data.repository.OrderRepository
import com.example.ordexa.domain.model.OrderStatus
import com.example.ordexa.domain.model.PurchaseOrder
import com.example.ordexa.ui.components.OrdexaTopAppBar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderListScreen(
    orderRepository: OrderRepository,
    onNavigateBack: () -> Unit,
    onSelectOrder: (String) -> Unit,
    onNavigateToAiAssistant: () -> Unit,
    onLogoutConfirmed: () -> Unit
) {
    val orders by orderRepository.orders.collectAsState(initial = emptyList())
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }
    val context = LocalContext.current

    val filteredOrders = orders.filter { order ->
        val matchesQuery = order.externalPoNumber.contains(searchQuery, ignoreCase = true) ||
                order.customerName.contains(searchQuery, ignoreCase = true)
        val matchesTab = when (selectedTab) {
            1 -> order.status == OrderStatus.APPROVED
            2 -> order.status == OrderStatus.RESERVED
            3 -> order.status == OrderStatus.PARTIALLY_DISPATCHED
            else -> true
        }
        matchesQuery && matchesTab
    }

    Scaffold(
        topBar = {
            OrdexaTopAppBar(
                title = "Order Management",
                onNavigateBack = onNavigateBack,
                onNavigateToAiAssistant = onNavigateToAiAssistant,
                onLogoutConfirmed = onLogoutConfirmed
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    val pdfParser = com.example.ordexa.domain.usecase.PdfParserUseCase(orderRepository)
                    val result = pdfParser.parseAndIngestPurchaseOrder("SAMPLE PDF STREAM NSC-PO-2026-0418")
                    if (result.isSuccess) {
                        Toast.makeText(context, "PO Ingested Successfully: ${result.order?.externalPoNumber}", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Ingestion Blocked: ${result.errorMessage}", Toast.LENGTH_LONG).show()
                    }
                },
                icon = { Icon(Icons.Default.PictureAsPdf, contentDescription = null) },
                text = { Text("Ingest PDF PO", fontWeight = FontWeight.Bold) },
                containerColor = Color(0xFFF5B027),
                contentColor = Color(0xFF1B262C)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF1F5F9))
                .padding(padding)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search PO Number or Customer...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF0F4C75)) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFF0F4C75),
                    unfocusedBorderColor = Color(0xFFE2E8F0)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 4.dp)
            )

            // Modern Single-Line Floating Filter Chips
            val filterOptions = listOf("All (${orders.size})", "Approved", "Reserved", "Dispatched")
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(filterOptions.size) { index ->
                    val isSelected = selectedTab == index
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        label = {
                            Text(
                                text = filterOptions[index],
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                                maxLines = 1
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF0F4C75),
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = Color(0xFF475569)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = Color(0xFFCBD5E1),
                            selectedBorderColor = Color(0xFF0F4C75),
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredOrders) { order ->
                    OrderItemCard(order = order, onClick = { onSelectOrder(order.id) })
                }
            }
        }
    }
}

@Composable
fun OrderItemCard(order: PurchaseOrder, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                Text(
                    text = order.externalPoNumber,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F4C75)
                )
                OrderStatusChip(status = order.status)
            }

            Text(
                text = order.customerName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B262C)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Date: ${order.orderDate} | Due: ${order.requestedDeliveryDate}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF64748B)
                )
                Text(
                    text = "₹${String.format(Locale.US, "%.2f", order.totalAmount)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1B262C)
                )
            }

            HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 4.dp))

            Surface(
                color = Color(0xFFF8FAFC),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "${order.lineItems.size} Line Items (${order.lineItems.joinToString { it.sku }})",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF475569),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun OrderStatusChip(status: OrderStatus) {
    val (bgColor, textColor) = when (status) {
        OrderStatus.APPROVED -> Pair(Color(0xFFD1FAE5), Color(0xFF065F46))
        OrderStatus.RESERVED -> Pair(Color(0xFFDBEAFE), Color(0xFF1E40AF))
        OrderStatus.PARTIALLY_DISPATCHED -> Pair(Color(0xFFFEF3C7), Color(0xFF92400E))
        OrderStatus.COMPLETED -> Pair(Color(0xFFCCFBF1), Color(0xFF115E59))
        else -> Pair(Color(0xFFE2E8F0), Color(0xFF475569))
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = status.label,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
