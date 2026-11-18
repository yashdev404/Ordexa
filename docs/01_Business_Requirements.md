# Ordexa — Business Requirements Document (BRD)

## 1. Executive Summary
**Ordexa** is an intelligent factory operations, inventory, and dispatch management platform designed for manufacturing facilities and multi-warehouse operations. 

The system operates on three non-negotiable engineering principles:
1. **Every order must be traceable.**
2. **Every stock movement must be accountable.**
3. **Every shipment must be verifiable.**

---

## 2. Business Domain & Operational Context

### 2.1 Factory Hierarchy
- **Company**: Top-level tenant (e.g., *Northstar Components Private Limited*).
- **Factory**: Production unit associated with a company (e.g., *Factory A*).
- **Warehouse**: Storage location belonging to a specific factory (e.g., *WH-A1*, *Baddi Receiving Bay 2*).
- **Bay / Location**: Specific storage bin or zone within a warehouse.

---

## 3. Ground-Truth Phase 0 Discovery Evidence & Sample Data

### 3.1 Sample Purchase Order Data (`NSC-PO-2026-0418`)
- **PO Number**: `NSC-PO-2026-0418`
- **Order Date**: 10-Nov-2026 | **Requested Delivery**: 14-Nov-2026 | **Priority**: Normal
- **Buyer / Company**: Northstar Components Pvt. Ltd. (Procurement Desk, RFQ-8821)
- **Supplier**: Apex Industrial Works Pvt. Ltd.
- **Ship To**: Receiving Bay 2, Plot 18, Sector 6, Baddi, Himachal Pradesh 173205
- **Order Line Items**:
  1. SKU `AX-100` | *Hex Bolt M10 x 40 mm* | UOM: PCS | Qty: 1,200 | Price: ₹4.50 | Total: ₹5,400.00
  2. SKU `AX-220` | *Steel Washer M10* | UOM: PCS | Qty: 2,400 | Price: ₹1.20 | Total: ₹2,880.00
  3. SKU `AX-315` | *Mounting Bracket Type B* | UOM: PCS | Qty: 300 | Price: ₹38.00 | Total: ₹11,400.00
- **Financials**: Subtotal: ₹19,680.00 | GST (18%): ₹3,542.40 | **Total: ₹23,222.40**
- **Delivery Instructions**: Deliver all line items to Baddi receiving bay. Do not split delivery without written approval. Mention PO number on delivery challan and vehicle documents.

---

### 3.2 Inventory Stock Ledger Sample Transactions

Ordexa uses a strict stock movement ledger model. Stock reservations do not alter physical balances; physical dispatches and damaged goods require logged transactions and manager sign-offs.

| Transaction ID | Date | Factory | Warehouse | SKU | Product | Type | Reference | Qty In | Qty Out | Balance After | Performed By | Approved By | Notes |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **STK-0001** | 2026-11-01 | Factory A | WH-A1 | AX-100 | Hex Bolt M10 x 40 mm | RECEIPT | GRN-778 | 2000 | 0 | 2000 | Ravi K. | Neha S. | Opening receipt |
| **STK-0002** | 2026-11-02 | Factory A | WH-A1 | AX-220 | Steel Washer M10 | RECEIPT | GRN-781 | 5000 | 0 | 5000 | Ravi K. | Neha S. | Opening receipt |
| **STK-0003** | 2026-11-03 | Factory A | WH-A1 | AX-100 | Hex Bolt M10 x 40 mm | RESERVATION | ORD-1001 | 0 | 0 | 2000 | System | N/A | Tracked separately, no physical stock-out |
| **STK-0004** | 2026-11-04 | Factory A | WH-A1 | AX-315 | Mounting Bracket Type B | RECEIPT | GRN-789 | 600 | 0 | 600 | Aman P. | Neha S. | Supplier receipt |
| **STK-0005** | 2026-11-05 | Factory A | WH-A1 | AX-220 | Steel Washer M10 | DISPATCH | SHP-2001 | 0 | 1200 | 3800 | Ravi K. | Dispatch Mgr | Shipment to Baddi |
| **STK-0006** | 2026-11-06 | Factory A | WH-A1 | AX-100 | Hex Bolt M10 x 40 mm | DAMAGE | DMG-004 | 0 | 12 | 1988 | Aman P. | Neha S. | 12 units damaged; approval required |

---

### 3.3 Core Discrepancy & Validation Scenarios

1. **Destination / Route Mismatch**:
   - *Scenario*: Order `ORD-2041` for Delhi assigned to Truck `TRK-018` operating on the Chandigarh route.
   - *Control*: Validate shipment destination against truck route before loading scan. Block loading until Dispatch Manager resolves mismatch.
2. **Extra Quantity Dispatched**:
   - *Scenario*: Order quantity 500 PCS, loaded quantity 520 PCS (+20 PCS variance).
   - *Control*: Require an explicit exception reason and Manager approval for quantity above order tolerance. Record ordered, picked, loaded, and dispatched quantities separately.
3. **Wrong SKU Picked**:
   - *Scenario*: Order line `AX-220` (Steel Washer M10) replaced by `AX-202` (Steel Washer M8).
   - *Control*: Require 1D/2D Barcode scan validation against exact SKU. Mismatched barcodes automatically halt loading.
4. **Duplicate Order Ingestion**:
   - *Scenario*: Incoming PDF `NSC-PO-2026-0418` uploaded again.
   - *Control*: Check uniqueness on `(company_id, customer_id, external_po_number)`. Flag duplicate orders for manual review.

---

### 3.4 Truck & Dispatch State Machine Rules

#### Dispatch State Transitions
```
DRAFT -> READY_FOR_ASSIGNMENT -> ASSIGNED -> LOADING -> LOADED -> VERIFIED -> DISPATCH_APPROVED -> DISPATCHED -> DELIVERED
                                                    |
                                                    +--> ON_HOLD / EXCEPTION_REVIEW
```

#### Eligibility Checks Before Truck Assignment
- **Vehicle Status**: Active, available, and not committed to an overlapping trip.
- **Capacity**: Load weight and volume within vehicle specs.
- **Route / Destination**: Route supports all shipment stop points.
- **Loading Verification**: Order ID and SKU barcode scanned before physical placement.
- **Multi-Order Support**: Multiple orders permitted per truck only if ship-to stops and segregation constraints are met.
- **Dispatch Sign-off**: Final approval requires 100% scan reconciliation, document attachment (Challan / E-Way Bill), and Dispatch Manager digital signature.

---

## 4. User Roles & Access Matrix

| Role | Operational Scope | Key Responsibilities |
| :--- | :--- | :--- |
| **Super Admin** | Company-wide | Tenant management, system configurations, global administration |
| **Operations Head** | Multi-Factory | Cross-factory oversight, performance analytics, policy enforcement |
| **Factory Manager** | Single Factory | Factory operations, order approvals, stock adjustment approvals |
| **Warehouse Manager** | Warehouse/Factory | Stock ledger oversight, receiving, stock reconciliation, low-stock management |
| **Dispatch Manager** | Factory Logistics | Truck assignment, dispatch verification, gate pass generation |
| **Field Manager** | Operations / Orders | Order status tracking, field monitoring, delivery confirmations |
| **Worker** | Assigned Tasks | Physical picking, scanning, inventory counting, loading |
