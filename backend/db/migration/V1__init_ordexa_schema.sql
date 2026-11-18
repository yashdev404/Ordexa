-- Ordexa Modular Monolith Database Schema (V1)
-- Environment: PostgreSQL 16+

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. Companies & Factories Hierarchy
CREATE TABLE companies (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    code VARCHAR(30) UNIQUE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE factories (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    company_id UUID NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(30) UNIQUE NOT NULL,
    location TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE warehouses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    factory_id UUID NOT NULL REFERENCES factories(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(30) UNIQUE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. Users, Roles & Multi-Factory Access
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    company_id UUID NOT NULL REFERENCES companies(id),
    email VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL, -- SUPER_ADMIN, OPERATIONS_HEAD, FACTORY_MANAGER, WAREHOUSE_MANAGER, DISPATCH_MANAGER, FIELD_MANAGER, WORKER
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_factory_roles (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    factory_id UUID NOT NULL REFERENCES factories(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, factory_id)
);

-- 3. Customers & Products
CREATE TABLE customers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    company_id UUID NOT NULL REFERENCES companies(id),
    name VARCHAR(150) NOT NULL,
    code VARCHAR(50) NOT NULL,
    ship_to_address TEXT,
    bill_to_address TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_customer_code UNIQUE (company_id, code)
);

CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    company_id UUID NOT NULL REFERENCES companies(id),
    sku VARCHAR(50) NOT NULL,
    name VARCHAR(150) NOT NULL,
    uom VARCHAR(20) DEFAULT 'PCS',
    unit_price NUMERIC(10,2) DEFAULT 0.00,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_product_sku UNIQUE (company_id, sku)
);

-- 4. Order Management
CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    company_id UUID NOT NULL REFERENCES companies(id),
    factory_id UUID NOT NULL REFERENCES factories(id),
    customer_id UUID NOT NULL REFERENCES customers(id),
    external_po_number VARCHAR(100) NOT NULL,
    order_date DATE NOT NULL,
    requested_delivery_date DATE,
    priority VARCHAR(20) DEFAULT 'Normal',
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT', -- DRAFT, VERIFIED, APPROVED, RESERVED, PARTIALLY_DISPATCHED, COMPLETED, CANCELLED
    supplier_name VARCHAR(150),
    ship_to_address TEXT,
    bill_to_address TEXT,
    subtotal NUMERIC(12,2) NOT NULL DEFAULT 0.00,
    gst_amount NUMERIC(12,2) NOT NULL DEFAULT 0.00,
    total_amount NUMERIC(12,2) NOT NULL DEFAULT 0.00,
    delivery_instructions TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_po_duplicate_prevention UNIQUE (company_id, customer_id, external_po_number)
);

CREATE TABLE order_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id UUID NOT NULL REFERENCES products(id),
    sku VARCHAR(50) NOT NULL,
    description TEXT,
    uom VARCHAR(20) DEFAULT 'PCS',
    quantity INT NOT NULL,
    unit_price NUMERIC(10,2) NOT NULL,
    total_amount NUMERIC(12,2) NOT NULL
);

-- 5. Inventory Stock & Immutable Transaction Ledger
CREATE TABLE inventory_stock (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    warehouse_id UUID NOT NULL REFERENCES warehouses(id) ON DELETE CASCADE,
    product_id UUID NOT NULL REFERENCES products(id),
    sku VARCHAR(50) NOT NULL,
    physical_qty INT NOT NULL DEFAULT 0,
    reserved_qty INT NOT NULL DEFAULT 0,
    version BIGINT NOT NULL DEFAULT 0, -- Optimistic locking
    CONSTRAINT uq_warehouse_product_stock UNIQUE (warehouse_id, product_id)
);

CREATE TABLE inventory_ledger (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    inventory_stock_id UUID NOT NULL REFERENCES inventory_stock(id),
    transaction_type VARCHAR(30) NOT NULL, -- RECEIPT, RESERVATION, PICK, DISPATCH, DAMAGE, ADJUSTMENT
    reference_type VARCHAR(30) NOT NULL,   -- GRN, ORDER, SHIPMENT, DAMAGE_REPORT, AUDIT
    reference_id VARCHAR(100) NOT NULL,
    qty_in INT NOT NULL DEFAULT 0,
    qty_out INT NOT NULL DEFAULT 0,
    balance_after INT NOT NULL,
    performed_by UUID NOT NULL REFERENCES users(id),
    approved_by UUID REFERENCES users(id),
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 6. Logistics, Trucks, Shipments & Discrepancies
CREATE TABLE trucks (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    factory_id UUID NOT NULL REFERENCES factories(id),
    vehicle_number VARCHAR(50) NOT NULL,
    driver_name VARCHAR(100) NOT NULL,
    driver_phone VARCHAR(20),
    capacity_weight_kg NUMERIC(10,2) DEFAULT 10000.00,
    route_name VARCHAR(100) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'Available'
);

CREATE TABLE shipments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    factory_id UUID NOT NULL REFERENCES factories(id),
    truck_id UUID REFERENCES trucks(id),
    shipment_number VARCHAR(50) NOT NULL UNIQUE,
    destination_city VARCHAR(100) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT', -- DRAFT, READY_FOR_ASSIGNMENT, ASSIGNED, LOADING, LOADED, VERIFIED, DISPATCH_APPROVED, DISPATCHED, DELIVERED, EXCEPTION_REVIEW
    total_units INT NOT NULL DEFAULT 0,
    loaded_units INT NOT NULL DEFAULT 0,
    created_by UUID NOT NULL REFERENCES users(id),
    approved_by UUID REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE discrepancy_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    shipment_id UUID REFERENCES shipments(id),
    order_id UUID REFERENCES orders(id),
    discrepancy_type VARCHAR(50) NOT NULL, -- DESTINATION_MISMATCH, EXTRA_QUANTITY, WRONG_SKU, DUPLICATE_PO
    expected_value TEXT NOT NULL,
    observed_value TEXT NOT NULL,
    detected_at_stage VARCHAR(50) NOT NULL,
    resolution_status VARCHAR(30) NOT NULL DEFAULT 'OPEN', -- OPEN, RESOLVED, REJECTED
    exception_reason TEXT,
    approved_by UUID REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for Fast Multi-Factory Querying & Audit Logs
CREATE INDEX idx_orders_factory ON orders(factory_id);
CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_inventory_stock_warehouse ON inventory_stock(warehouse_id);
CREATE INDEX idx_inventory_ledger_stock ON inventory_ledger(inventory_stock_id);
CREATE INDEX idx_shipments_factory ON shipments(factory_id);
CREATE INDEX idx_discrepancies_shipment ON discrepancy_logs(shipment_id);
