package com.example.ordexa.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ordexa.data.mock.MockData
import com.example.ordexa.domain.model.Factory
import com.example.ordexa.ui.components.OrdexaTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    currentFactory: Factory,
    onNavigateToOrders: () -> Unit,
    onNavigateToInventory: () -> Unit,
    onNavigateToDispatch: () -> Unit,
    onNavigateToAiAssistant: () -> Unit,
    onNavigateToApprovalHub: () -> Unit,
    onNavigateToEodSummary: () -> Unit,
    onLogoutConfirmed: () -> Unit
) {
    Scaffold(
        topBar = {
            OrdexaTopAppBar(
                title = "ORDEXA",
                currentFactory = currentFactory,
                onNavigateToAiAssistant = onNavigateToAiAssistant,
                onLogoutConfirmed = onLogoutConfirmed
            )
        },
        floatingActionButton = {
            // Dedicated Floating Circular AI Sparkle FAB (WhatsApp / Zomato Style)
            Surface(
                onClick = onNavigateToAiAssistant,
                shape = CircleShape,
                shadowElevation = 10.dp,
                modifier = Modifier.size(60.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF8B5CF6), Color(0xFF6366F1), Color(0xFF0F4C75))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = "Ordexa AI Assistant",
                        tint = Color(0xFFF5B027),
                        modifier = Modifier.size(28.dp)
                    )
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
            // Welcome Banner Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B262C))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                color = Color(0xFFF5B027),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = MockData.currentUser.role.displayName.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B262C),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Welcome back, ${MockData.currentUser.name}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Factory Scope: ${currentFactory.code} (${currentFactory.location})",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF0F4C75),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFFF5B027), modifier = Modifier.size(28.dp))
                            }
                        }
                    }
                }
            }

            // High-Level KPIs
            item {
                Text(
                    text = "Real-Time Factory KPIs",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF475569)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    KpiCard(
                        title = "Active Orders",
                        value = "${MockData.sampleOrders.size}",
                        subtitle = "1 Pending Approval",
                        icon = Icons.Default.Receipt,
                        accentColor = Color(0xFF0F4C75),
                        bgColor = Color(0xFFE2EFF8),
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToOrders
                    )
                    KpiCard(
                        title = "Stock Balance",
                        value = "${MockData.sampleInventoryStock.sumOf { it.physicalQty }}",
                        subtitle = "${MockData.sampleInventoryStock.sumOf { it.availableQty }} Available",
                        icon = Icons.Default.Inventory2,
                        accentColor = Color(0xFF0D9488),
                        bgColor = Color(0xFFCCFBF1),
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToInventory
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    KpiCard(
                        title = "Active Shipments",
                        value = "${MockData.sampleShipments.size}",
                        subtitle = "1 Approved Gate Pass",
                        icon = Icons.Default.LocalShipping,
                        accentColor = Color(0xFF3B82F6),
                        bgColor = Color(0xFFDBEAFE),
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToDispatch
                    )
                    KpiCard(
                        title = "Discrepancies",
                        value = "${MockData.sampleDiscrepancies.count { it.resolutionStatus == "OPEN" }} Open",
                        subtitle = "Route & Extra Qty",
                        icon = Icons.Default.Warning,
                        accentColor = Color(0xFFD97706),
                        bgColor = Color(0xFFFEF3C7),
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToDispatch
                    )
                }
            }

            // Quick Modules Section
            item {
                Text(
                    text = "Operational Modules",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF475569)
                )
            }

            item {
                ModuleActionRow(
                    title = "Pending Authority Hub",
                    subtitle = "3 items awaiting sign-off (Damage reports, Route bypasses)",
                    icon = Icons.Default.Shield,
                    badgeColor = Color(0xFFEF4444),
                    onClick = onNavigateToApprovalHub
                )
            }

            item {
                ModuleActionRow(
                    title = "EOD Shift Closing Summary",
                    subtitle = "Review daily receipts, shipped destination matrix & seal shift",
                    icon = Icons.Default.Lock,
                    badgeColor = Color(0xFF0F4C75),
                    onClick = onNavigateToEodSummary
                )
            }

            item {
                ModuleActionRow(
                    title = "Order Management & PDF Ingestion",
                    subtitle = "Ingest POs, verify line items (AX-100, AX-220, AX-315)",
                    icon = Icons.Default.Description,
                    badgeColor = Color(0xFF0F4C75),
                    onClick = onNavigateToOrders
                )
            }

            item {
                ModuleActionRow(
                    title = "Inventory Stock Ledger",
                    subtitle = "Track stock receipts, reservations & damaged goods",
                    icon = Icons.Default.Warehouse,
                    badgeColor = Color(0xFFF5B027),
                    onClick = onNavigateToInventory
                )
            }

            item {
                ModuleActionRow(
                    title = "Dispatch & Barcode Verification",
                    subtitle = "Assign trucks, scan SKU barcodes, verify gate pass",
                    icon = Icons.Default.QrCodeScanner,
                    badgeColor = Color(0xFF10B981),
                    onClick = onNavigateToDispatch
                )
            }
        }
    }
}

@Composable
fun KpiCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    bgColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = bgColor,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = accentColor)
                    }
                }
            }
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1B262C))
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = accentColor, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ModuleActionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badgeColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = badgeColor.copy(alpha = 0.12f),
                modifier = Modifier.size(52.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = badgeColor, modifier = Modifier.size(26.dp))
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF1B262C))
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color(0xFF94A3B8))
        }
    }
}
