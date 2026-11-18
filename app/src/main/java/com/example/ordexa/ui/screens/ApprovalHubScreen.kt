package com.example.ordexa.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Shield
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
import com.example.ordexa.domain.model.ApprovalStatus
import com.example.ordexa.domain.model.PendingApprovalItem
import com.example.ordexa.ui.components.OrdexaTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApprovalHubScreen(
    approvalRepository: ApprovalRepository,
    onNavigateBack: () -> Unit,
    onNavigateToAiAssistant: () -> Unit,
    onLogoutConfirmed: () -> Unit
) {
    val pendingList by approvalRepository.pendingApprovals.collectAsState(initial = emptyList())
    var selectedTab by remember { mutableStateOf(0) }
    val context = LocalContext.current

    val filteredList = pendingList.filter { item ->
        when (selectedTab) {
            0 -> item.status == ApprovalStatus.PENDING
            1 -> item.status == ApprovalStatus.APPROVED
            2 -> item.status == ApprovalStatus.REJECTED
            else -> true
        }
    }

    Scaffold(
        topBar = {
            OrdexaTopAppBar(
                title = "Pending Authority Hub",
                onNavigateBack = onNavigateBack,
                onNavigateToAiAssistant = onNavigateToAiAssistant,
                onLogoutConfirmed = onLogoutConfirmed
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF1F5F9))
                .padding(padding)
        ) {
            // Header Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B262C))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = Color(0xFFF5B027), modifier = Modifier.size(36.dp))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Elevated Approvals Queue",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Authority Role: ${MockData.currentUser.role.displayName} (${MockData.currentUser.name})",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // Single-Line Floating Filter Chips
            val filterOptions = listOf(
                "Pending (${pendingList.count { it.status == ApprovalStatus.PENDING }})",
                "Approved (${pendingList.count { it.status == ApprovalStatus.APPROVED }})",
                "Rejected (${pendingList.count { it.status == ApprovalStatus.REJECTED }})"
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                items(filteredList) { item ->
                    ApprovalItemCard(
                        item = item,
                        onApprove = {
                            approvalRepository.approveItem(item.id, MockData.currentUser.name)
                            Toast.makeText(context, "Authorized: ${item.type.label}", Toast.LENGTH_SHORT).show()
                        },
                        onReject = {
                            approvalRepository.rejectItem(item.id, MockData.currentUser.name)
                            Toast.makeText(context, "Rejected: ${item.type.label}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ApprovalItemCard(
    item: PendingApprovalItem,
    onApprove: () -> Unit,
    onReject: () -> Unit
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
                Surface(
                    color = Color(0xFFFFF3D6),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = item.type.label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF4A3400),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                ApprovalStatusChip(status = item.status)
            }

            Text("Ref: ${item.referenceId} | Requested By: ${item.requestedBy}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color(0xFF0F4C75))
            Text(item.details, style = MaterialTheme.typography.bodySmall, color = Color(0xFF1B262C))

            Surface(
                color = Color(0xFFF8FAFC),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Impact: ${item.impactSummary}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF475569),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }

            if (item.status == ApprovalStatus.PENDING) {
                HorizontalDivider(color = Color(0xFFF1F5F9))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onReject,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444))
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Reject", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onApprove,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F4C75))
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFFF5B027), modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Authorize", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Text(
                    text = "Signed off by ${item.approvedBy ?: "Manager"} (${item.actionNotes})",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF10B981),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ApprovalStatusChip(status: ApprovalStatus) {
    val (bgColor, textColor) = when (status) {
        ApprovalStatus.APPROVED -> Pair(Color(0xFFD1FAE5), Color(0xFF065F46))
        ApprovalStatus.REJECTED -> Pair(Color(0xFFFEE2E2), Color(0xFF991B1B))
        ApprovalStatus.PENDING -> Pair(Color(0xFFFEF3C7), Color(0xFF92400E))
    }

    Surface(color = bgColor, shape = RoundedCornerShape(12.dp)) {
        Text(status.label, color = textColor, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
    }
}
