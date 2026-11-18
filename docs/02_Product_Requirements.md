# Ordexa — Product Requirements Document (PRD)

## 1. Product Scope & Vision

### 1.1 MVP Scope (Build First)
- **Multi-Tenant / Multi-Factory Support**: Strict data isolation at the factory level.
- **RBAC & Authorization**: Fine-grained role permissions and factory-scoped resource access.
- **PDF Order Ingestion**: Automatic parsing & manual verification UI for standard Purchase Orders (e.g., `NSC-PO-2026-0418`).
- **Inventory Stock Ledger**: Immutable transactional stock movements (`RECEIPT`, `RESERVATION`, `PICK`, `DISPATCH`, `DAMAGE`, `ADJUSTMENT`).
- **Dispatch & Logistics Management**: Truck eligibility verification, multi-stop route checking, 1D/2D barcode scanning, exception approval workflows, and digital gate passes.
- **Audit Logging**: Immutable history for orders, stock movements, and dispatch gate passes.
- **AI Operations Assistant**: Natural-language stock queries, PDF extraction validation, shipment summaries.
- **Android Application**: Mobile interface for factory workers, warehouse managers, and dispatch managers with CameraX/MLKit barcode scanning.

### 1.2 Future Scope (Ordexa 2.0)
- Inter-factory stock transfers & multi-hop logistics.
- Offline-first Android sync for dead-zone warehouse areas.
- Predictive demand forecasting & automated reorder points.
- Multi-language support (Regional languages for factory workers).

---

## 2. Detailed Functional Requirements

### 2.1 Authentication & Security (AUTH)
- **AUTH-1**: User login via username/email & password with JWT token generation containing `company_id`, `role`, and `permitted_factory_ids`.
- **AUTH-2**: Multi-factor authorization for critical actions (e.g., stock adjustment approvals, extra quantity dispatch sign-off).
- **AUTH-3**: Factory-level context switching for users assigned to multiple factories.

### 2.2 Order Management & PDF Ingestion (ORD)
- **ORD-1**: Ingest PDF orders via web portal or Android app upload.
- **ORD-2**: Extract Purchase Order Metadata:
  - Header: `external_po_number`, `order_date`, `requested_delivery_date`, `priority`, `buyer_contact`, `reference_number`
  - Parties: `supplier_name`, `ship_to_address`, `bill_to_address`
  - Line Items: `sku`, `description`, `unit_of_measure`, `quantity`, `unit_price`, `amount`
  - Totals: `subtotal`, `gst_amount`, `tax_rate`, `total_amount`
  - Notes: `delivery_instructions`
- **ORD-3**: Flag potential duplicate orders using `(company_id, customer_id, external_po_number)` uniqueness checks.
- **ORD-4**: Maintain order state machine: `DRAFT` -> `VERIFIED` -> `APPROVED` -> `RESERVED` -> `PARTIALLY_DISPATCHED` -> `COMPLETED` / `CANCELLED`.

### 2.3 Inventory Management & Stock Ledger (INV)
- **INV-1**: Maintain an immutable `InventoryLedger` for every stock transaction.
- **INV-2**: Separate `physical_quantity` from `reserved_quantity`. Formula: `Available Qty = Physical Qty - Reserved Qty`.
- **INV-3**: Transaction types supported:
  - `RECEIPT`: Increase physical stock (Goods Received Note / GRN).
  - `RESERVATION`: Increase reserved stock count upon order approval (physical stock unchanged).
  - `PICK`: Move stock from warehouse bay to staging area.
  - `DISPATCH`: Decrease physical & reserved stock upon dispatch sign-off.
  - `DAMAGE`: Decrease physical stock upon manager approval (with reason code).
  - `ADJUSTMENT`: Reconcile physical inventory after cycle count.
- **INV-4**: Require Manager approval (`Approved By`) for `DAMAGE` or `ADJUSTMENT` transactions.
- **INV-5**: Low-stock threshold alerts per SKU per warehouse.

### 2.4 Dispatch & Logistics (DSP)
- **DSP-1**: Vehicle / Truck registration: Vehicle Number, Driver Name, Phone Number, Carrier, Capacity (Weight/Volume), Route.
- **DSP-2**: State Machine:
  `DRAFT` -> `READY_FOR_ASSIGNMENT` -> `ASSIGNED` -> `LOADING` -> `LOADED` -> `VERIFIED` -> `DISPATCH_APPROVED` -> `DISPATCHED` -> `DELIVERED`.
- **DSP-3**: Barcode / QR Code Scanning:
  - Scan SKU barcode (`AX-100`, `AX-220`, `AX-315`) during truck loading.
  - Reject wrong SKU scans (e.g., loading `AX-202` instead of `AX-220`).
- **DSP-4**: Exception & Discrepancy Handling:
  - Destination Mismatch (e.g., Delhi order loaded onto Chandigarh truck): Pause loading, place shipment on `ON_HOLD` or `EXCEPTION_REVIEW`.
  - Extra Quantity Loaded (e.g., 520 PCS loaded for 500 PCS order): Block loading until Dispatch Manager inputs exception reason and signs off.
- **DSP-5**: Generate digital dispatch gate pass upon final Dispatch Manager approval.

### 2.5 Android Application (MOB)
- **MOB-1**: Role-based navigation (Worker UI for Picking/Loading vs Manager UI for Approvals/Overview).
- **MOB-2**: Touch-friendly Compose UI built with Material 3 components.
- **MOB-3**: High-performance Camera Barcode/QR code scanner integration.
- **MOB-4**: Offline caching via Room database for low-connectivity warehouse zones.

### 2.6 AI Operational Assistant (AI)
- **AI-1**: LLM-assisted PDF parsing for unstructured order documents.
- **AI-2**: Controlled tool-calling for natural language queries (e.g., *"Show current available balance for SKU AX-100 in WH-A1"*).
- **AI-3**: Automated shipment summaries and discrepancy reporting.

---

## 3. Non-Functional Requirements (NFR)

- **NFR-1 Security & Data Isolation**: Database queries MUST be scoped by `company_id` and `factory_id`. Data leaks between factories are P0 security defects.
- **NFR-2 Concurrency Control**: Inventory reservations and ledger entries MUST use pessimistic database locking (`SELECT FOR UPDATE`) or optimistic version checking (`@Version`) to prevent lost updates or negative available balances.
- **NFR-3 Auditability**: All state mutations must log `user_id`, `timestamp`, `ip_address`, `action`, `resource_type`, and `factory_id`.
- **NFR-4 Performance**: Mobile API responses < 300ms; Barcode scan validation < 100ms.
- **NFR-5 Usability**: Android UI components designed with minimum 48dp touch targets and high-visibility feedback for industrial handhelds.
