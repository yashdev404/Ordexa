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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ordexa.data.repository.InventoryRepository
import com.example.ordexa.domain.model.InventoryLedger
import com.example.ordexa.domain.model.InventoryStock
import com.example.ordexa.domain.model.TransactionType

import com.example.ordexa.ui.components.OrdexaTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    inventoryRepository: InventoryRepository,
    onNavigateBack: () -> Unit,
    onNavigateToAiAssistant: () -> Unit,
    onLogoutConfirmed: () -> Unit
) {
    val stockList by inventoryRepository.stockList.collectAsState(initial = emptyList())
    val ledgerList by inventoryRepository.ledgerList.collectAsState(initial = emptyList())
    var selectedTab by remember { mutableStateOf(0) }
    val context = LocalContext.current

    var showReceiptDialog by remember { mutableStateOf(false) }
    var showDamageDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            OrdexaTopAppBar(
                title = "Inventory Stock Ledger",
                onNavigateBack = onNavigateBack,
                onNavigateToAiAssistant = onNavigateToAiAssistant,
                onLogoutConfirmed = onLogoutConfirmed
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp, color = Color.White, modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { showReceiptDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F4C75))
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFFF5B027))
                        Spacer(Modifier.width(4.dp))
                        Text("Record GRN", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { showDamageDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.5.dp, Color(0xFFEF4444)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444))
                    ) {
                        Icon(Icons.Default.ReportProblem, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Report Damage", fontWeight = FontWeight.Bold)
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
            // Single-Line Floating Filter Chips
            val filterOptions = listOf("Stock Balances", "Ledger History (${ledgerList.size})")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                filterOptions.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        label = {
                            Text(
                                text = title,
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

            if (selectedTab == 0) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE2EFF8))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Rule: Available Stock = Physical Qty - Reserved Qty",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF052136)
                                )
                            }
                        }
                    }
                    items(stockList) { stock ->
                        StockItemCard(stock = stock)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(ledgerList) { ledger ->
                        LedgerRowCard(ledger = ledger)
                    }
                }
            }
        }

        // Receipt Modal Dialog
        if (showReceiptDialog) {
            RecordReceiptDialog(
                stockList = stockList,
                onDismiss = { showReceiptDialog = false },
                onConfirm = { sku, qty, grn ->
                    val success = inventoryRepository.recordReceipt(sku, qty, grn, "Ravi K.", "Neha S.")
                    if (success) {
                        Toast.makeText(context, "Logged GRN $grn for $sku (+ $qty PCS)", Toast.LENGTH_SHORT).show()
                    }
                    showReceiptDialog = false
                }
            )
        }

        // Damage Report Modal Dialog
        if (showDamageDialog) {
            ReportDamageDialog(
                stockList = stockList,
                onDismiss = { showDamageDialog = false },
                onConfirm = { sku, qty, dmgRef, notes ->
                    val success = inventoryRepository.recordDamage(sku, qty, dmgRef, "Aman P.", "Neha S.", notes)
                    if (success) {
                        Toast.makeText(context, "Logged Damage $dmgRef for $sku (- $qty PCS)", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error: Insufficient stock!", Toast.LENGTH_SHORT).show()
                    }
                    showDamageDialog = false
                }
            )
        }
    }
}

@Composable
fun StockItemCard(stock: InventoryStock) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                Text(stock.sku, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F4C75))
                Surface(
                    color = Color(0xFFFFF3D6),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "WH: ${stock.warehouseName}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A3400),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(stock.productName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = Color(0xFF1B262C))

            HorizontalDivider(color = Color(0xFFF1F5F9))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Physical Stock", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
                    Text("${stock.physicalQty} ${stock.unitOfMeasure}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold)
                }
                Column {
                    Text("Reserved Stock", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
                    Text("${stock.reservedQty} ${stock.unitOfMeasure}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E40AF))
                }
                Column {
                    Text("Available Stock", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
                    Text("${stock.availableQty} ${stock.unitOfMeasure}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold, color = Color(0xFF065F46))
                }
            }
        }
    }
}

@Composable
fun LedgerRowCard(ledger: InventoryLedger) {
    val (typeColor, typeText) = when (ledger.transactionType) {
        TransactionType.RECEIPT -> Pair(Color(0xFF065F46), "+${ledger.qtyIn}")
        TransactionType.RESERVATION -> Pair(Color(0xFF1E40AF), "RESERVED")
        TransactionType.DISPATCH -> Pair(Color(0xFF92400E), "-${ledger.qtyOut}")
        TransactionType.DAMAGE -> Pair(Color(0xFF991B1B), "-${ledger.qtyOut}")
        else -> Pair(Color(0xFF1B262C), "${ledger.qtyIn - ledger.qtyOut}")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${ledger.id} | ${ledger.sku}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F4C75))
                Text(ledger.transactionType.name, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.ExtraBold, color = typeColor)
            }

            Text("${ledger.productName} — Ref: ${ledger.reference}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF1B262C))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Qty: $typeText | Bal After: ${ledger.balanceAfter}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                Text("By: ${ledger.performedBy} (Appr: ${ledger.approvedBy ?: "N/A"})", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
            }

            if (ledger.notes.isNotEmpty()) {
                Text("Notes: ${ledger.notes}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
            }
        }
    }
}

@Composable
fun RecordReceiptDialog(
    stockList: List<InventoryStock>,
    onDismiss: () -> Unit,
    onConfirm: (sku: String, qty: Int, grn: String) -> Unit
) {
    var selectedSku by remember { mutableStateOf(stockList.firstOrNull()?.sku ?: "") }
    var qtyText by remember { mutableStateOf("500") }
    var grnText by remember { mutableStateOf("GRN-795") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Record Goods Receipt (GRN)", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = selectedSku,
                    onValueChange = { selectedSku = it },
                    label = { Text("SKU (e.g., AX-100)") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = qtyText,
                    onValueChange = { qtyText = it },
                    label = { Text("Receipt Quantity") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = grnText,
                    onValueChange = { grnText = it },
                    label = { Text("GRN Reference No.") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val qty = qtyText.toIntOrNull() ?: 0
                    if (selectedSku.isNotEmpty() && qty > 0) {
                        onConfirm(selectedSku, qty, grnText)
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F4C75))
            ) {
                Text("Record GRN", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun ReportDamageDialog(
    stockList: List<InventoryStock>,
    onDismiss: () -> Unit,
    onConfirm: (sku: String, qty: Int, dmgRef: String, notes: String) -> Unit
) {
    var selectedSku by remember { mutableStateOf(stockList.firstOrNull()?.sku ?: "") }
    var qtyText by remember { mutableStateOf("10") }
    var dmgRefText by remember { mutableStateOf("DMG-008") }
    var notesText by remember { mutableStateOf("Packaging crushed during unloading") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Report Damaged Goods", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = selectedSku,
                    onValueChange = { selectedSku = it },
                    label = { Text("SKU") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = qtyText,
                    onValueChange = { qtyText = it },
                    label = { Text("Damaged Quantity") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dmgRefText,
                    onValueChange = { dmgRefText = it },
                    label = { Text("Damage Report Ref No.") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notesText,
                    onValueChange = { notesText = it },
                    label = { Text("Damage Notes / Reason") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val qty = qtyText.toIntOrNull() ?: 0
                    if (selectedSku.isNotEmpty() && qty > 0) {
                        onConfirm(selectedSku, qty, dmgRefText, notesText)
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
            ) {
                Text("Submit Damage Report", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
