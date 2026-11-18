package com.example.ordexa.domain.model

enum class ApprovalType(val label: String) {
    DAMAGE_REPORT("Damage Report Sign-off"),
    ROUTE_MISMATCH("Route Mismatch Bypass"),
    EXTRA_QUANTITY("Extra Quantity Tolerance"),
    STOCK_ADJUSTMENT("Stock Inventory Reconciliation")
}

enum class ApprovalStatus(val label: String) {
    PENDING("Pending Approval"),
    APPROVED("Approved"),
    REJECTED("Rejected")
}

data class PendingApprovalItem(
    val id: String,
    val type: ApprovalType,
    val referenceId: String,
    val requestedBy: String,
    val requestedAt: String,
    val details: String,
    val impactSummary: String,
    var status: ApprovalStatus = ApprovalStatus.PENDING,
    var approvedBy: String? = null,
    var actionNotes: String? = null
)

data class DestinationSummary(
    val city: String,
    val totalUnitsShipped: Int,
    val truckNumber: String,
    val shipmentNumber: String,
    val poNumber: String
)

data class EodShiftReport(
    val shiftId: String,
    val date: String,
    val factoryId: String,
    val factoryName: String,
    val managerName: String,
    val totalInboundGrnUnits: Int,
    val totalOutboundShippedUnits: Int,
    val totalDamagedUnits: Int,
    val pendingApprovalsCount: Int,
    val destinationBreakdown: List<DestinationSummary>,
    var isSealed: Boolean = false,
    var sealedAt: String? = null,
    var sealedBy: String? = null
)
