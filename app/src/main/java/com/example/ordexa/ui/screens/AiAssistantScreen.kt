package com.example.ordexa.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ordexa.data.repository.DispatchRepository
import com.example.ordexa.data.repository.InventoryRepository
import com.example.ordexa.data.repository.OrderRepository
import com.example.ordexa.ui.components.OrdexaTopAppBar
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

data class ChatMessage(
    val id: String,
    val sender: String, // "USER" or "AI"
    val text: String,
    val timestamp: String = "Just now"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAssistantScreen(
    orderRepository: OrderRepository,
    inventoryRepository: InventoryRepository,
    dispatchRepository: DispatchRepository,
    onNavigateBack: () -> Unit,
    onLogoutConfirmed: () -> Unit
) {
    var queryInput by remember { mutableStateOf("") }
    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage(
                    id = "MSG-0",
                    sender = "AI",
                    text = "Hello! I am Ordexa AI Assistant. You can ask me natural language queries like:\n- 'What is the stock balance for AX-100?'\n- 'Show any open discrepancies'\n- 'Summarize active shipments'"
                )
            )
        )
    }

    Scaffold(
        topBar = {
            OrdexaTopAppBar(
                title = "Ordexa AI Assistant",
                onNavigateBack = onNavigateBack,
                onLogoutConfirmed = onLogoutConfirmed
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 12.dp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = queryInput,
                        onValueChange = { queryInput = it },
                        placeholder = { Text("Ask AI Assistant about stock, orders, dispatches...") },
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF8FAFC),
                            unfocusedContainerColor = Color(0xFFF8FAFC),
                            focusedBorderColor = Color(0xFF0F4C75),
                            unfocusedBorderColor = Color(0xFFE2E8F0)
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {
                            if (queryInput.isNotBlank()) {
                                val userText = queryInput.trim()
                                val userMsg = ChatMessage(id = "MSG-${messages.size}", sender = "USER", text = userText)
                                messages = messages + userMsg
                                queryInput = ""

                                val aiReply = processAiQuery(userText, orderRepository, inventoryRepository, dispatchRepository)
                                val aiMsg = ChatMessage(id = "MSG-${messages.size + 1}", sender = "AI", text = aiReply)
                                messages = messages + aiMsg
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFF0F4C75), shape = CircleShape)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF1F5F9))
                .padding(padding)
        ) {
            // Quick Suggestion Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = false,
                    onClick = {
                        queryInput = "Stock status of AX 100"
                    },
                    label = { Text("AX-100 Status", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold) },
                    shape = RoundedCornerShape(16.dp),
                    colors = FilterChipDefaults.filterChipColors(containerColor = Color.White)
                )
                FilterChip(
                    selected = false,
                    onClick = {
                        queryInput = "Show open discrepancies"
                    },
                    label = { Text("Open Discrepancies", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold) },
                    shape = RoundedCornerShape(16.dp),
                    colors = FilterChipDefaults.filterChipColors(containerColor = Color.White)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { msg ->
                    ChatBubble(message = msg)
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val isUser = message.sender == "USER"
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) Color(0xFF0F4C75) else Color.White
            ),
            border = if (!isUser) BorderStroke(1.dp, Color(0xFFE2E8F0)) else null,
            modifier = Modifier.widthIn(max = 320.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                if (!isUser) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFFF5B027), modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Ordexa AI Engine", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F4C75))
                    }
                    Spacer(Modifier.height(6.dp))
                }

                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isUser) Color.White else Color(0xFF1B262C),
                    fontWeight = if (isUser) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

private fun processAiQuery(
    query: String,
    orderRepository: OrderRepository,
    inventoryRepository: InventoryRepository,
    dispatchRepository: DispatchRepository
): String = runBlocking {
    val raw = query.lowercase().replace(" ", "").replace("-", "").replace("_", "")
    return@runBlocking when {
        raw.contains("ax100") || raw.contains("hexbolt") -> {
            val stock = inventoryRepository.stockList.first().find { it.sku == "AX-100" }
            if (stock != null) {
                "[Stock Analysis] SKU AX-100 (Hex Bolt M10 x 40 mm):\n- Current Status: Active Stock\n- Physical Stock: ${stock.physicalQty} PCS\n- Reserved Stock: ${stock.reservedQty} PCS\n- Available Balance: ${stock.availableQty} PCS\n- Location: WH-A1 (Main Warehouse)"
            } else "SKU AX-100 not found in database."
        }
        raw.contains("ax220") || raw.contains("steelwasher") -> {
            val stock = inventoryRepository.stockList.first().find { it.sku == "AX-220" }
            if (stock != null) {
                "[Stock Analysis] SKU AX-220 (Steel Washer M10):\n- Current Status: Active Stock\n- Physical Stock: ${stock.physicalQty} PCS\n- Reserved Stock: ${stock.reservedQty} PCS\n- Available Balance: ${stock.availableQty} PCS\n- Location: WH-A1 (Main Warehouse)"
            } else "SKU AX-220 not found."
        }
        raw.contains("discrepanc") -> {
            val discList = dispatchRepository.discrepancies.first().filter { it.resolutionStatus == "OPEN" }
            if (discList.isNotEmpty()) {
                val d = discList.first()
                "[Alert] 1 Open Discrepancy Found:\n- Type: ${d.discrepancyType}\n- Shipment: ${d.shipmentId} (Order: ${d.orderId})\n- Expected: ${d.expectedValue}\n- Observed: ${d.observedValue}\n- Stage: ${d.detectedAtStage}\n- Action: Requires Dispatch Manager sign-off."
            } else "All discrepancies are resolved."
        }
        raw.contains("shipment") || raw.contains("truck") || raw.contains("dispatch") -> {
            val shipList = dispatchRepository.shipments.first()
            "[Logistics Summary] Total Shipments: ${shipList.size}\n- SHP-2001: Approved Gate Pass to Baddi\n- SHP-2041: Exception Review (Delhi Route mismatch)"
        }
        raw.contains("order") || raw.contains("po") || raw.contains("northstar") -> {
            val orders = orderRepository.orders.first()
            val nsc = orders.find { it.externalPoNumber.contains("0418") }
            if (nsc != null) {
                "[Purchase Order] Northstar PO ${nsc.externalPoNumber}:\n- Customer: ${nsc.customerName}\n- Status: ${nsc.status.label}\n- Total Amount: ₹${String.format(java.util.Locale.US, "%.2f", nsc.totalAmount)}\n- Line Items: ${nsc.lineItems.joinToString { "${it.sku} (${it.quantity} PCS)" }}"
            } else "Orders count: ${orders.size}"
        }
        else -> "I analyzed your operational database. I can help with stock status (e.g., 'AX-100', 'AX-220'), open discrepancies, shipments, or PO verification."
    }
}
