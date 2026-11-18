package com.example.ordexa.data.mock

import com.example.ordexa.domain.model.*

object MockData {
    val sampleFactories = listOf(
        Factory("FACT-A", "Factory A — Baddi Plant", "F-A", "Plot 18, Sector 6, Baddi, HP 173205"),
        Factory("FACT-B", "Factory B — Chandigarh Hub", "F-B", "Industrial Area Phase 1, Chandigarh")
    )

    val sampleWarehouses = listOf(
        Warehouse("WH-A1", "FACT-A", "Main Warehouse A1", "WH-A1"),
        Warehouse("WH-B1", "FACT-B", "Logistics Hub B1", "WH-B1")
    )

    val currentUser = User(
        id = "USR-001",
        name = "Neha Sharma",
        email = "neha.sharma@ordexa.com",
        role = UserRole.FACTORY_MANAGER,
        companyId = "COMP-001",
        permittedFactoryIds = listOf("FACT-A", "FACT-B")
    )

    val sampleOrders = mutableListOf(
        PurchaseOrder(
            id = "ORD-NSC-0418",
            externalPoNumber = "NSC-PO-2026-0418",
            customerName = "Northstar Components Pvt. Ltd.",
            orderDate = "2026-11-10",
            requestedDeliveryDate = "2026-11-14",
            priority = "Normal",
            status = OrderStatus.APPROVED,
            supplierName = "Apex Industrial Works Pvt. Ltd.",
            shipToAddress = "Receiving Bay 2, Plot 18, Sector 6, Baddi, HP 173205",
            billToAddress = "Northstar Components Pvt. Ltd., Accounts Dept, Chandigarh",
            lineItems = listOf(
                OrderItem("LI-101", "AX-100", "Hex Bolt M10 x 40 mm", "PCS", 1200, 4.50, 5400.00),
                OrderItem("LI-102", "AX-220", "Steel Washer M10", "PCS", 2400, 1.20, 2880.00),
                OrderItem("LI-103", "AX-315", "Mounting Bracket Type B", "PCS", 300, 38.00, 11400.00)
            ),
            subtotal = 19680.00,
            gstAmount = 3542.40,
            totalAmount = 23222.40,
            deliveryInstructions = "Deliver all line items to Baddi receiving bay. Do not split delivery without written approval. Mention PO number on delivery challan."
        ),
        PurchaseOrder(
            id = "ORD-1001",
            externalPoNumber = "PO-2026-1001",
            customerName = "Vardhman Industries",
            orderDate = "2026-11-03",
            requestedDeliveryDate = "2026-11-12",
            priority = "High",
            status = OrderStatus.RESERVED,
            supplierName = "Factory A Assembly",
            shipToAddress = "Plot 44, Industrial Estate, Ludhiana",
            billToAddress = "Vardhman H.O., Ludhiana",
            lineItems = listOf(
                OrderItem("LI-201", "AX-100", "Hex Bolt M10 x 40 mm", "PCS", 2000, 4.50, 9000.00)
            ),
            subtotal = 9000.00,
            gstAmount = 1620.00,
            totalAmount = 10620.00,
            deliveryInstructions = "Reserved stock allocation active."
        ),
        PurchaseOrder(
            id = "ORD-2041",
            externalPoNumber = "PO-2026-2041",
            customerName = "Delhi Engineering Supplies",
            orderDate = "2026-11-05",
            requestedDeliveryDate = "2026-11-15",
            priority = "Urgent",
            status = OrderStatus.PARTIALLY_DISPATCHED,
            supplierName = "Factory A Assembly",
            shipToAddress = "Warehouse 4, Okhla Phase 3, New Delhi",
            billToAddress = "Delhi Engineering Supplies, Nehru Place, New Delhi",
            lineItems = listOf(
                OrderItem("LI-301", "AX-100", "Hex Bolt M10 x 40 mm", "PCS", 500, 4.50, 2250.00)
            ),
            subtotal = 2250.00,
            gstAmount = 405.00,
            totalAmount = 2655.00,
            deliveryInstructions = "Route validation required before dispatch."
        )
    )

    val sampleInventoryStock = mutableListOf(
        InventoryStock("STK-ITEM-1", "WH-A1", "Main Warehouse A1", "AX-100", "Hex Bolt M10 x 40 mm", 1988, 1200),
        InventoryStock("STK-ITEM-2", "WH-A1", "Main Warehouse A1", "AX-220", "Steel Washer M10", 3800, 2400),
        InventoryStock("STK-ITEM-3", "WH-A1", "Main Warehouse A1", "AX-315", "Mounting Bracket Type B", 600, 300),
        InventoryStock("STK-ITEM-4", "WH-A1", "Main Warehouse A1", "AX-202", "Steel Washer M8", 1500, 0)
    )

    val sampleLedgerTransactions = mutableListOf(
        InventoryLedger("STK-0001", "2026-11-01", "Factory A", "WH-A1", "AX-100", "Hex Bolt M10 x 40 mm", TransactionType.RECEIPT, "GRN-778", 2000, 0, 2000, "Ravi K.", "Neha S.", "Opening receipt"),
        InventoryLedger("STK-0002", "2026-11-02", "Factory A", "WH-A1", "AX-220", "Steel Washer M10", TransactionType.RECEIPT, "GRN-781", 5000, 0, 5000, "Ravi K.", "Neha S.", "Opening receipt"),
        InventoryLedger("STK-0003", "2026-11-03", "Factory A", "WH-A1", "AX-100", "Hex Bolt M10 x 40 mm", TransactionType.RESERVATION, "ORD-1001", 0, 0, 2000, "System", null, "Reservation tracked separately, not as physical stock-out"),
        InventoryLedger("STK-0004", "2026-11-04", "Factory A", "WH-A1", "AX-315", "Mounting Bracket Type B", TransactionType.RECEIPT, "GRN-789", 600, 0, 600, "Aman P.", "Neha S.", "Supplier receipt"),
        InventoryLedger("STK-0005", "2026-11-05", "Factory A", "WH-A1", "AX-220", "Steel Washer M10", TransactionType.DISPATCH, "SHP-2001", 0, 1200, 3800, "Ravi K.", "Dispatch Mgr", "Shipment to Baddi"),
        InventoryLedger("STK-0006", "2026-11-06", "Factory A", "WH-A1", "AX-100", "Hex Bolt M10 x 40 mm", TransactionType.DAMAGE, "DMG-004", 0, 12, 1988, "Aman P.", "Neha S.", "12 units damaged; approval required")
    )

    val sampleTrucks = listOf(
        Truck("TRK-005", "HP-12-AB-4501", "Gurmeet Singh", "+91 98160 11223", "Baddi Local Route", 5000.0, "Assigned"),
        Truck("TRK-018", "PB-65-AX-9912", "Sukhdev Sharma", "+91 98722 44331", "Chandigarh Route", 9000.0, "Available"),
        Truck("TRK-022", "DL-01-GA-3019", "Rajesh Kumar", "+91 98100 77889", "Delhi NCR Route", 12000.0, "Available")
    )

    val sampleShipments = mutableListOf(
        Shipment(
            id = "SHP-2001",
            shipmentNumber = "SHP-2026-2001",
            factoryId = "FACT-A",
            destinationCity = "Baddi",
            status = ShipmentStatus.DISPATCH_APPROVED,
            truck = sampleTrucks[0],
            assignedOrderIds = listOf("ORD-NSC-0418"),
            totalUnits = 3900,
            loadedUnits = 3900,
            createdBy = "Ravi K.",
            approvedBy = "Dispatch Mgr"
        ),
        Shipment(
            id = "SHP-2041",
            shipmentNumber = "SHP-2026-2041",
            factoryId = "FACT-A",
            destinationCity = "Delhi",
            status = ShipmentStatus.EXCEPTION_REVIEW,
            truck = sampleTrucks[1], // Route mismatch: Chandigarh truck assigned to Delhi
            assignedOrderIds = listOf("ORD-2041"),
            totalUnits = 500,
            loadedUnits = 500,
            createdBy = "Aman P.",
            approvedBy = null
        )
    )

    val sampleDiscrepancies = mutableListOf(
        DiscrepancyLog(
            id = "DISC-001",
            shipmentId = "SHP-2041",
            orderId = "ORD-2041",
            discrepancyType = "DESTINATION_MISMATCH",
            expectedValue = "Delhi Route (TRK-022)",
            observedValue = "TRK-018 assigned to Chandigarh Route",
            detectedAtStage = "Loading Scan",
            resolutionStatus = "OPEN",
            exceptionReason = "Truck assignment mismatch before departure",
            approvedBy = null
        ),
        DiscrepancyLog(
            id = "DISC-002",
            shipmentId = "SHP-2001",
            orderId = "ORD-NSC-0418",
            discrepancyType = "EXTRA_QUANTITY",
            expectedValue = "500 PCS",
            observedValue = "520 PCS (+20 PCS variance)",
            detectedAtStage = "Loading Scan",
            resolutionStatus = "RESOLVED",
            exceptionReason = "Approved over-delivery tolerance by Customer Desk",
            approvedBy = "Neha S."
        ),
        DiscrepancyLog(
            id = "DISC-003",
            shipmentId = "SHP-2001",
            orderId = "ORD-NSC-0418",
            discrepancyType = "WRONG_SKU",
            expectedValue = "AX-220 (Steel Washer M10)",
            observedValue = "AX-202 (Steel Washer M8)",
            detectedAtStage = "Barcode Scan",
            resolutionStatus = "RESOLVED",
            exceptionReason = "Scanned item rejected; correct SKU replaced",
            approvedBy = "Ravi K."
        )
    )

    val samplePendingApprovals = mutableListOf(
        PendingApprovalItem(
            id = "APPR-001",
            type = ApprovalType.DAMAGE_REPORT,
            referenceId = "DMG-004",
            requestedBy = "Aman P. (Worker)",
            requestedAt = "2026-11-12 14:30",
            details = "12 units of AX-100 Hex Bolt damaged during unloading.",
            impactSummary = "Physical Stock reduction (-12 PCS, ₹54.00 loss)"
        ),
        PendingApprovalItem(
            id = "APPR-002",
            type = ApprovalType.ROUTE_MISMATCH,
            referenceId = "SHP-2041",
            requestedBy = "Ravi K. (Warehouse Mgr)",
            requestedAt = "2026-11-12 16:15",
            details = "TRK-018 (Chandigarh Route) assigned to Delhi Shipment SHP-2041.",
            impactSummary = "Logistics Route Override Required"
        ),
        PendingApprovalItem(
            id = "APPR-003",
            type = ApprovalType.EXTRA_QUANTITY,
            referenceId = "SHP-2001",
            requestedBy = "Ravi K. (Warehouse Mgr)",
            requestedAt = "2026-11-12 17:00",
            details = "+20 PCS Steel Washer M10 loaded over 500 PCS order quantity.",
            impactSummary = "Over-delivery tolerance override (+4% variance)"
        )
    )

    val sampleEodReport = EodShiftReport(
        shiftId = "SHIFT-2026-11-12-A",
        date = "2026-11-12",
        factoryId = "FACT-A",
        factoryName = "Factory A — Baddi Plant",
        managerName = "Neha Sharma",
        totalInboundGrnUnits = 7600,
        totalOutboundShippedUnits = 4400,
        totalDamagedUnits = 12,
        pendingApprovalsCount = 3,
        destinationBreakdown = listOf(
            DestinationSummary("Baddi Receiving Bay", 3900, "HP-12-AB-4501 (TRK-005)", "SHP-2026-2001", "NSC-PO-2026-0418"),
            DestinationSummary("Delhi Logistics Hub", 500, "DL-01-GA-3019 (TRK-022)", "SHP-2026-2041", "PO-2026-2041")
        ),
        isSealed = false
    )
}
