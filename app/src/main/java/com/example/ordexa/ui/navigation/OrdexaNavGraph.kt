package com.example.ordexa.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ordexa.data.mock.MockData
import com.example.ordexa.data.repository.ApprovalRepository
import com.example.ordexa.data.repository.DispatchRepository
import com.example.ordexa.data.repository.InventoryRepository
import com.example.ordexa.data.repository.OrderRepository
import com.example.ordexa.ui.screens.AiAssistantScreen
import com.example.ordexa.ui.screens.ApprovalHubScreen
import com.example.ordexa.ui.screens.AuthScreen
import com.example.ordexa.ui.screens.DashboardScreen
import com.example.ordexa.ui.screens.DispatchScreen
import com.example.ordexa.ui.screens.EodSummaryScreen
import com.example.ordexa.ui.screens.InventoryScreen
import com.example.ordexa.ui.screens.OrderDetailScreen
import com.example.ordexa.ui.screens.OrderListScreen

@Composable
fun OrdexaNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val orderRepository = remember { OrderRepository() }
    val inventoryRepository = remember { InventoryRepository() }
    val dispatchRepository = remember { DispatchRepository() }
    val approvalRepository = remember { ApprovalRepository() }

    val currentFactory = remember { MockData.sampleFactories.first() }

    val onNavigateToAiAssistant = {
        navController.navigate(Screen.AiAssistant.route)
    }

    val onLogoutConfirmed = {
        navController.navigate(Screen.Login.route) {
            popUpTo(Screen.Dashboard.route) { inclusive = true }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            AuthScreen(
                onLoginSuccess = { _ ->
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                currentFactory = currentFactory,
                onNavigateToOrders = { navController.navigate(Screen.OrderList.route) },
                onNavigateToInventory = { navController.navigate(Screen.Inventory.route) },
                onNavigateToDispatch = { navController.navigate(Screen.Dispatch.route) },
                onNavigateToAiAssistant = onNavigateToAiAssistant,
                onNavigateToApprovalHub = { navController.navigate(Screen.ApprovalHub.route) },
                onNavigateToEodSummary = { navController.navigate(Screen.EodSummary.route) },
                onLogoutConfirmed = onLogoutConfirmed
            )
        }

        composable(Screen.OrderList.route) {
            OrderListScreen(
                orderRepository = orderRepository,
                onNavigateBack = { navController.popBackStack() },
                onSelectOrder = { orderId ->
                    navController.navigate(Screen.OrderDetail.createRoute(orderId))
                },
                onNavigateToAiAssistant = onNavigateToAiAssistant,
                onLogoutConfirmed = onLogoutConfirmed
            )
        }

        composable(Screen.OrderDetail.route) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderDetailScreen(
                orderId = orderId,
                orderRepository = orderRepository,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAiAssistant = onNavigateToAiAssistant,
                onLogoutConfirmed = onLogoutConfirmed
            )
        }

        composable(Screen.Inventory.route) {
            InventoryScreen(
                inventoryRepository = inventoryRepository,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAiAssistant = onNavigateToAiAssistant,
                onLogoutConfirmed = onLogoutConfirmed
            )
        }

        composable(Screen.Dispatch.route) {
            DispatchScreen(
                dispatchRepository = dispatchRepository,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAiAssistant = onNavigateToAiAssistant,
                onLogoutConfirmed = onLogoutConfirmed
            )
        }

        composable(Screen.AiAssistant.route) {
            AiAssistantScreen(
                orderRepository = orderRepository,
                inventoryRepository = inventoryRepository,
                dispatchRepository = dispatchRepository,
                onNavigateBack = { navController.popBackStack() },
                onLogoutConfirmed = onLogoutConfirmed
            )
        }

        composable(Screen.ApprovalHub.route) {
            ApprovalHubScreen(
                approvalRepository = approvalRepository,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAiAssistant = onNavigateToAiAssistant,
                onLogoutConfirmed = onLogoutConfirmed
            )
        }

        composable(Screen.EodSummary.route) {
            EodSummaryScreen(
                approvalRepository = approvalRepository,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAiAssistant = onNavigateToAiAssistant,
                onLogoutConfirmed = onLogoutConfirmed
            )
        }
    }
}
