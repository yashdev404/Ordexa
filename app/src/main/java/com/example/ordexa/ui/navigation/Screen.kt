package com.example.ordexa.ui.navigation

sealed class Screen(val route: String, val title: String) {
    object Login : Screen("login", "Login")
    object FactorySelect : Screen("factory_select", "Select Factory")
    object Dashboard : Screen("dashboard", "Ordexa Dashboard")
    object OrderList : Screen("orders", "Orders")
    object OrderDetail : Screen("order_detail/{orderId}", "Order Detail") {
        fun createRoute(orderId: String) = "order_detail/$orderId"
    }
    object Inventory : Screen("inventory", "Inventory Ledger")
    object Dispatch : Screen("dispatch", "Dispatch & Logistics")
    object AiAssistant : Screen("ai_assistant", "AI Assistant")
    object ApprovalHub : Screen("approval_hub", "Pending Approvals Hub")
    object EodSummary : Screen("eod_summary", "EOD Shift Summary")
    object Profile : Screen("profile", "Employee Profile")
}
