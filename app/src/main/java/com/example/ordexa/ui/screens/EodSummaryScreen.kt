package com.example.ordexa.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ordexa.data.mock.MockData
import com.example.ordexa.data.repository.ApprovalRepository
import com.example.ordexa.domain.model.DestinationSummary
import com.example.ordexa.domain.model.EodShiftReport
import com.example.ordexa.ui.components.OrdexaTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EodSummaryScreen(
    approvalRepository: ApprovalRepository,
    onNavigateBack: () -> Unit,
    onNavigateToAiAssistant: () -> Unit,
    onLogoutConfirmed: () -> Unit
) {
    val report by approvalRepository.eodReport.collectAsState(initial = MockData.sampleEodReport)
    val context = LocalContext.current

    Scaffold(
        topBar = {
            OrdexaTopAppBar(
                title = "EOD Shift Closing Summary",
                onNavigateBack = onNavigateBack,
                onNavigateToAiAssistant = onNavigateToAiAssistant,
                onLogoutConfirmed = onLogoutConfirmed
            )
        },
        bottomBar = {
            Surface(shadowElevation = 12.dp, color = Color.White, modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    if (!report.isSealed) {
                        Button(
                            onClick = {
                                approvalRepository.sealEodShift(MockData.currentUser.name)
                                Toast.makeText(context, "EOD Shift Report Sealed & Signed Off!", Toast.LENGTH_LONG).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F4C75))
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFFF5B027))
                            Spacer(Modifier.width(8.dp))
                            Text("Seal & Sign-Off EOD Shift Ledger", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Surface(
                            color = Color(0xFFD1FAE5),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF065F46))
                                Spacer(Modifier.width(8.dp))
                                Column {
                                    Text("EOD Shift Ledger Sealed & Locked", fontWeight = FontWeight.ExtraBold, color = Color(0xFF065F46))
                                    Text("Sealed by ${report.sealedBy} on ${report.sealedAt}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF065F46))
                                }
                            }
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
            // Header Shift Info Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B262C))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = Color(0xFFF5B027),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = report.shiftId,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF1B262C),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                            Text(report.date, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
                        }

                        Text(report.factoryName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Factory Manager: ${report.managerName}", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.7f))
                    }
                }
            }

            // Executive KPI Grid
            item {
                Text("Shift Performance Totals", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    EodKpiMetric(
                        title = "Inbound Stock Added",
                        value = "+${report.totalInboundGrnUnits} PCS",
                        subtitle = "Total GRN Receipts",
                        icon = Icons.Default.Description,
                        accentColor = Color(0xFF065F46),
                        bgColor = Color(0xFFD1FAE5),
                        modifier = Modifier.weight(1f)
                    )
                    EodKpiMetric(
                        title = "Outbound Shipped",
                        value = "${report.totalOutboundShippedUnits} PCS",
                        subtitle = "Total Dispatches",
                        icon = Icons.Default.LocalShipping,
                        accentColor = Color(0xFF0F4C75),
                        bgColor = Color(0xFFE2EFF8),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    EodKpiMetric(
                        title = "Damaged Stock Loss",
                        value = "${report.totalDamagedUnits} PCS",
                        subtitle = "Logged Damage Reports",
                        icon = Icons.Default.ReportProblem,
                        accentColor = Color(0xFF991B1B),
                        bgColor = Color(0xFFFEE2E2),
                        modifier = Modifier.weight(1f)
                    )
                    EodKpiMetric(
                        title = "Pending Approvals",
                        value = "${report.pendingApprovalsCount} Items",
                        subtitle = "Authority Queue",
                        icon = Icons.Default.Lock,
                        accentColor = Color(0xFF92400E),
                        bgColor = Color(0xFFFEF3C7),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Where Stock Went Today Matrix
            item {
                Text("Where Stock Went Today (Destination Matrix)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
            }

            items(report.destinationBreakdown) { dest ->
                DestinationRowCard(dest = dest)
            }
        }
    }
}

@Composable
fun EodKpiMetric(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
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
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1B262C))
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = accentColor, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DestinationRowCard(dest: DestinationSummary) {
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
                Text(dest.city, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F4C75))
                Surface(
                    color = Color(0xFFE2EFF8),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${dest.totalUnitsShipped} Units",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F4C75),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text("Truck: ${dest.truckNumber}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = Color(0xFF1B262C))
            Text("Manifest: ${dest.shipmentNumber} | PO: ${dest.poNumber}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
        }
    }
}
