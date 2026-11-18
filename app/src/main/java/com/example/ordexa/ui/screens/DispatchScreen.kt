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
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ordexa.data.repository.DispatchRepository
import com.example.ordexa.domain.model.DiscrepancyLog
import com.example.ordexa.domain.model.Shipment
import com.example.ordexa.domain.model.ShipmentStatus
import com.example.ordexa.domain.model.Truck

import com.example.ordexa.ui.components.OrdexaTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DispatchScreen(
    dispatchRepository: DispatchRepository,
    onNavigateBack: () -> Unit,
    onNavigateToAiAssistant: () -> Unit,
    onLogoutConfirmed: () -> Unit
) {
    val shipments by dispatchRepository.shipments.collectAsState(initial = emptyList())
    val discrepancies by dispatchRepository.discrepancies.collectAsState(initial = emptyList())
    val trucks = dispatchRepository.availableTrucks

    var selectedTab by remember { mutableStateOf(0) }
    var selectedShipmentForAssign by remember { mutableStateOf<Shipment?>(null) }
    var showScanSimulatorDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            OrdexaTopAppBar(
                title = "Dispatch & Barcode Verification",
                onNavigateBack = onNavigateBack,
                onNavigateToAiAssistant = onNavigateToAiAssistant,
                onLogoutConfirmed = onLogoutConfirmed
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showScanSimulatorDialog = true },
                icon = { Icon(Icons.Default.QrCodeScanner, contentDescription = null) },
                text = { Text("Scan SKU Barcode", fontWeight = FontWeight.Bold) },
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
            // Single-Line Floating Filter Chips
            val filterOptions = listOf("Shipments (${shipments.size})", "Discrepancies (${discrepancies.size})")
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
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(shipments) { shipment ->
                        ShipmentCard(
                            shipment = shipment,
                            onAssignTruck = { selectedShipmentForAssign = shipment },
                            onApproveDispatch = {
                                dispatchRepository.approveDispatch(shipment.id, "Dispatch Mgr")
                                Toast.makeText(context, "Gate Pass Generated for ${shipment.shipmentNumber}!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(discrepancies) { discrepancy ->
                        DiscrepancyCard(
                            discrepancy = discrepancy,
                            onResolve = {
                                dispatchRepository.resolveDiscrepancy(discrepancy.id, "Neha S.", "Approved by Factory Manager after verification")
                                Toast.makeText(context, "Resolved ${discrepancy.id}", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }

        // Truck Assignment Dialog with Route Validation Check
        if (selectedShipmentForAssign != null) {
            val shipment = selectedShipmentForAssign!!
            AssignTruckDialog(
                shipment = shipment,
                trucks = trucks,
                onDismiss = { selectedShipmentForAssign = null },
                onSelectTruck = { truck ->
                    val errorMsg = dispatchRepository.assignTruckToShipment(shipment.id, truck)
                    if (errorMsg != null) {
                        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Assigned ${truck.vehicleNumber} to ${shipment.shipmentNumber}", Toast.LENGTH_SHORT).show()
                    }
                    selectedShipmentForAssign = null
                }
            )
        }

        // Barcode Scanner Simulator Dialog
        if (showScanSimulatorDialog) {
            BarcodeScanSimulatorDialog(
                onDismiss = { showScanSimulatorDialog = false },
                onScanResult = { sku ->
                    if (sku == "AX-100" || sku == "AX-220" || sku == "AX-315") {
                        Toast.makeText(context, "[Verified] Barcode $sku matches Shipment Manifest", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "[Mismatch Alert] Scanned $sku is NOT in shipment order list", Toast.LENGTH_LONG).show()
                    }
                    showScanSimulatorDialog = false
                }
            )
        }
    }
}

@Composable
fun ShipmentCard(
    shipment: Shipment,
    onAssignTruck: () -> Unit,
    onApproveDispatch: () -> Unit
) {
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
                Text(shipment.shipmentNumber, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F4C75))
                ShipmentStatusChip(status = shipment.status)
            }

            Text("Destination: ${shipment.destinationCity}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color(0xFF1B262C))

            if (shipment.truck != null) {
                Surface(
                    color = Color(0xFFE2EFF8),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocalShipping, contentDescription = null, tint = Color(0xFF0F4C75))
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("Truck: ${shipment.truck.vehicleNumber} (${shipment.truck.driverName})", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color(0xFF052136))
                            Text("Route: ${shipment.truck.routeName} | Driver: ${shipment.truck.driverPhone}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF0F4C75))
                        }
                    }
                }
            } else {
                Text("No truck assigned yet.", style = MaterialTheme.typography.bodySmall, color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
            }

            Text("Units Loaded: ${shipment.loadedUnits} / ${shipment.totalUnits}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)

            HorizontalDivider(color = Color(0xFFF1F5F9))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (shipment.status == ShipmentStatus.DRAFT || shipment.status == ShipmentStatus.READY_FOR_ASSIGNMENT || shipment.status == ShipmentStatus.EXCEPTION_REVIEW) {
                    OutlinedButton(
                        onClick = onAssignTruck,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (shipment.truck == null) "Assign Truck" else "Re-assign Truck", fontWeight = FontWeight.Bold)
                    }
                }

                if (shipment.status == ShipmentStatus.VERIFIED || shipment.status == ShipmentStatus.LOADED || shipment.status == ShipmentStatus.ASSIGNED || shipment.status == ShipmentStatus.DISPATCH_APPROVED) {
                    Button(
                        onClick = onApproveDispatch,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F4C75))
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFFF5B027))
                        Spacer(Modifier.width(4.dp))
                        Text("Approve Gate Pass", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun DiscrepancyCard(
    discrepancy: DiscrepancyLog,
    onResolve: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (discrepancy.resolutionStatus == "OPEN") Color(0xFFFEF2F2)
            else Color.White
        ),
        border = BorderStroke(1.dp, if (discrepancy.resolutionStatus == "OPEN") Color(0xFFFCA5A5) else Color(0xFFE2E8F0))
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
                Text(discrepancy.discrepancyType, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold, color = Color(0xFF991B1B))
                Surface(
                    color = if (discrepancy.resolutionStatus == "OPEN") Color(0xFFEF4444) else Color(0xFF10B981),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(discrepancy.resolutionStatus, color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp))
                }
            }

            Text("Shipment: ${discrepancy.shipmentId} | Order: ${discrepancy.orderId}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = Color(0xFF1B262C))
            Text("Expected: ${discrepancy.expectedValue}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF475569))
            Text("Observed: ${discrepancy.observedValue}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1B262C))

            if (discrepancy.exceptionReason != null) {
                Text("Reason: ${discrepancy.exceptionReason}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF0F4C75), fontWeight = FontWeight.Bold)
            }

            if (discrepancy.resolutionStatus == "OPEN") {
                Button(
                    onClick = onResolve,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Resolve Exception & Authorize", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ShipmentStatusChip(status: ShipmentStatus) {
    val (bgColor, textColor) = when (status) {
        ShipmentStatus.DISPATCH_APPROVED -> Pair(Color(0xFFD1FAE5), Color(0xFF065F46))
        ShipmentStatus.EXCEPTION_REVIEW, ShipmentStatus.ON_HOLD -> Pair(Color(0xFFFEE2E2), Color(0xFF991B1B))
        ShipmentStatus.ASSIGNED -> Pair(Color(0xFFDBEAFE), Color(0xFF1E40AF))
        else -> Pair(Color(0xFFE2E8F0), Color(0xFF475569))
    }

    Surface(color = bgColor, shape = RoundedCornerShape(12.dp)) {
        Text(status.label, color = textColor, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
    }
}

@Composable
fun AssignTruckDialog(
    shipment: Shipment,
    trucks: List<Truck>,
    onDismiss: () -> Unit,
    onSelectTruck: (Truck) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Assign Truck to ${shipment.shipmentNumber}", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Destination: ${shipment.destinationCity}", fontWeight = FontWeight.Bold, color = Color(0xFF0F4C75))
                Text("Select vehicle from list:", style = MaterialTheme.typography.labelSmall)

                trucks.forEach { truck ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        onClick = { onSelectTruck(truck) }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("${truck.vehicleNumber} — ${truck.driverName}", fontWeight = FontWeight.ExtraBold, color = Color(0xFF1B262C))
                            Text("Route: ${truck.routeName} (Capacity: ${truck.capacityKg} kg)", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun BarcodeScanSimulatorDialog(
    onDismiss: () -> Unit,
    onScanResult: (String) -> Unit
) {
    var scannedBarcode by remember { mutableStateOf("AX-100") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("1D/2D Barcode Scanner", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Simulate camera recognition of SKU Barcode:")

                OutlinedTextField(
                    value = scannedBarcode,
                    onValueChange = { scannedBarcode = it },
                    label = { Text("Scanned Barcode / SKU Code") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = scannedBarcode == "AX-100", onClick = { scannedBarcode = "AX-100" }, label = { Text("AX-100") })
                    FilterChip(selected = scannedBarcode == "AX-220", onClick = { scannedBarcode = "AX-220" }, label = { Text("AX-220") })
                    FilterChip(selected = scannedBarcode == "AX-202", onClick = { scannedBarcode = "AX-202" }, label = { Text("AX-202 (Mismatched)") })
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onScanResult(scannedBarcode) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F4C75))
            ) {
                Text("Validate Scan", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
