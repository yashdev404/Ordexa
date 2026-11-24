-- Seed Ground-Truth Phase 0 Sample Data

INSERT INTO companies (id, name, code)
VALUES ('00000000-0000-0000-0000-000000000001', 'Northstar Components Private Limited', 'NORTHSTAR')
ON CONFLICT DO NOTHING;

INSERT INTO factories (id, company_id, name, code, location)
VALUES ('00000000-0000-0000-0000-000000000010', '00000000-0000-0000-0000-000000000001', 'Factory A — Baddi Plant', 'FACT-A', 'Plot 18, Sector 6, Baddi, HP 173205')
ON CONFLICT DO NOTHING;

INSERT INTO warehouses (id, factory_id, name, code)
VALUES ('00000000-0000-0000-0000-000000000100', '00000000-0000-0000-0000-000000000010', 'Main Warehouse A1', 'WH-A1')
ON CONFLICT DO NOTHING;

INSERT INTO users (id, company_id, email, name, password_hash, role)
VALUES ('00000000-0000-0000-0000-000000001000', '00000000-0000-0000-0000-000000000001', 'neha.sharma@ordexa.com', 'Neha Sharma', '$2a$10$e8p2H1Q1vY3gHk6qV.mJ9e9uW7H.vK.wZ2L5vX4P.Y7Z.aB.cD.eF', 'FACTORY_MANAGER')
ON CONFLICT DO NOTHING;
