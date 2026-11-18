package com.example.ordexa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ordexa.data.mock.MockData
import com.example.ordexa.domain.model.Factory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onLoginSuccess: (Factory) -> Unit
) {
    var email by remember { mutableStateOf("neha.sharma@ordexa.com") }
    var password by remember { mutableStateOf("") }
    var selectedFactory by remember { mutableStateOf(MockData.sampleFactories.first()) }
    var expandedFactoryMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B262C)), // Brand Dark Slate
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Brand Logo Badge
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF5B027), // Industrial Gold Accent
                    modifier = Modifier.size(68.dp),
                    shadowElevation = 4.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "O",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF1B262C)
                        )
                    }
                }

                Text(
                    text = "ORDEXA",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 3.sp,
                    color = Color(0xFF0F4C75) // Steel Blue
                )

                Text(
                    text = "Intelligent Factory & Supply Chain Operations",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Factory Selector Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedFactoryMenu,
                    onExpandedChange = { expandedFactoryMenu = !expandedFactoryMenu }
                ) {
                    OutlinedTextField(
                        value = selectedFactory.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Factory Workspace", fontWeight = FontWeight.Bold) },
                        leadingIcon = { Icon(Icons.Default.Business, contentDescription = null, tint = Color(0xFF0F4C75)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFactoryMenu) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedFactoryMenu,
                        onDismissRequest = { expandedFactoryMenu = false }
                    ) {
                        MockData.sampleFactories.forEach { factory ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(factory.name, fontWeight = FontWeight.Bold)
                                        Text(factory.location, style = MaterialTheme.typography.labelSmall)
                                    }
                                },
                                onClick = {
                                    selectedFactory = factory
                                    expandedFactoryMenu = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email / Employee ID") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF0F4C75)) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF0F4C75)) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onLoginSuccess(selectedFactory) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0F4C75),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Sign In to Workspace",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    color = Color(0xFFFFF3D6),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Demo User: ${MockData.currentUser.name} (${MockData.currentUser.role.displayName})",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF4A3400),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}
