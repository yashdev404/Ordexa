package com.example.ordexa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ordexa.data.mock.MockData
import com.example.ordexa.domain.model.Factory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdexaTopAppBar(
    title: String,
    subtitle: String? = null,
    currentFactory: Factory? = null,
    onNavigateBack: (() -> Unit)? = null,
    onNavigateToAiAssistant: (() -> Unit)? = null,
    onLogoutConfirmed: () -> Unit
) {
    var showProfileMenu by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val user = MockData.currentUser

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (onNavigateBack != null) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(Modifier.width(4.dp))
                } else {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF5B027),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("O", fontWeight = FontWeight.Bold, color = Color(0xFF1B262C), fontSize = 18.sp)
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                }

                Column {
                    Text(title, fontWeight = FontWeight.ExtraBold, letterSpacing = 0.5.sp)
                    val sub = subtitle ?: currentFactory?.name
                    if (sub != null) {
                        Text(sub, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }
        },
        actions = {
            if (onNavigateToAiAssistant != null) {
                IconButton(onClick = onNavigateToAiAssistant) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF8B5CF6).copy(alpha = 0.25f),
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = "Ordexa AI Assistant",
                                tint = Color(0xFFF5B027),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            IconButton(onClick = {}) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
            }

            Box {
                IconButton(onClick = { showProfileMenu = true }) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFF5B027),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = user.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF1B262C),
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                // Profile Dropdown Popover Menu
                DropdownMenu(
                    expanded = showProfileMenu,
                    onDismissRequest = { showProfileMenu = false },
                    modifier = Modifier
                        .width(280.dp)
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF0F4C75),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFFF5B027))
                                }
                            }
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text(user.name, fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleMedium, color = Color(0xFF1B262C))
                                Surface(
                                    color = Color(0xFFFFF3D6),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = user.role.displayName,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4A3400),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))

                        Text("Employee ID: ${user.id}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                        Text("Email: ${user.email}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
                        if (currentFactory != null) {
                            Text("Active Workspace: ${currentFactory.name}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF0F4C75), fontWeight = FontWeight.Bold)
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))

                        // Logout Dropdown Menu Item
                        DropdownMenuItem(
                            text = {
                                Text("Log Out of Ordexa", fontWeight = FontWeight.Bold, color = Color(0xFFEF4444))
                            },
                            leadingIcon = {
                                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = Color(0xFFEF4444))
                            },
                            onClick = {
                                showProfileMenu = false
                                showLogoutDialog = true
                            }
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF0F4C75),
            titleContentColor = Color.White
        )
    )

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Sign Out of Ordexa?", fontWeight = FontWeight.ExtraBold) },
            text = {
                Text(
                    "Are you sure you want to sign out of ${currentFactory?.name ?: "your active session"}? You will need to enter your credentials to log in again.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF334155)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutConfirmed()
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444), contentColor = Color.White)
                ) {
                    Text("Yes, Sign Out", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showLogoutDialog = false },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Cancel", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
