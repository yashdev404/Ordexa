# Ordexa - Intelligent Factory Operations, Inventory, and Dispatch Management System

Ordexa is an enterprise-grade factory operations, multi-warehouse inventory, and dispatch management platform. The platform is designed around three core operational principles:

1. Every order must be traceable.
2. Every stock movement must be accountable.
3. Every shipment must be verifiable.


---

## System Architecture Overview

Ordexa operates on a Client-Server modular architecture. The Android client communicates with the backend service via REST APIs over HTTPS, managing local offline persistence and real-time operational workflows.

```
+-------------------------------------------------------------------+
|                        Ordexa Mobile (Android)                    |
|          Kotlin + Jetpack Compose + Room + StateFlow + Retrofit   |
+-------------------------------------------------------------------+
                                  |
                                  | REST API (HTTPS / JSON)
                                  v
+-------------------------------------------------------------------+
|                     Ordexa Backend (Spring Boot 3)                |
|                                                                   |
|  +---------------+  +---------------+  +-----------------------+  |
|  | Security & Auth|  | Factory & Org |  | Order Management      |  |
|  | Module (JWT)  |  | Module        |  | Module (PDF/State)    |  |
|  +---------------+  +---------------+  +-----------------------+  |
|  +---------------+  +---------------+  +-----------------------+  |
|  | Inventory     |  | Dispatch &    |  | AI Assistant          |  |
|  | Ledger Module |  | Logistics     |  | Module (Tool Calling) |  |
|  +---------------+  +---------------+  +-----------------------+  |
+-------------------------------------------------------------------+
                                  |
               +------------------+------------------+
               |                                     |
               v                                     v
   +-----------------------+             +-----------------------+
   | PostgreSQL Database   |             | LLM / Document OCR    |
   | (Flyway Migrations)   |             | Service API           |
   +-----------------------+             +-----------------------+
```

---

## Frontend - Android Application

The frontend is a native Android application built using modern Android development practices, declarative UI, and Clean Architecture.

### Directory Structure

```
com.example.ordexa/
├── data/
│   ├── local/
│   │   ├── entities/        # Room Database Entities (DispatchEntity, InventoryEntity, OrderEntity)
│   │   └── OrdexaDatabase   # Local Room Database Configuration
│   ├── mock/                # Mock Data for Off-Network Testing & Demos
│   ├── remote/              # Retrofit API Interfaces & DTOs
│   └── repository/          # OrderRepository, InventoryRepository, DispatchRepository, ApprovalRepository
├── domain/
│   ├── model/               # Core Domain Entities (User, OrderModel, InventoryModel, DispatchModel, EodModel)
│   └── usecase/             # Business Logic (PdfParserUseCase, BarcodeVerificationUseCase)
├── ui/
│   ├── components/          # Reusable UI Components (OrdexaTopAppBar, StatCards, InputFields)
│   ├── navigation/          # Navigation Graph & Route Definitions (OrdexaNavGraph, Screen)
│   ├── screens/             # Jetpack Compose Screens
│   │   ├── AiAssistantScreen.kt
│   │   ├── ApprovalHubScreen.kt
│   │   ├── AuthScreen.kt
│   │   ├── DashboardScreen.kt
│   │   ├── DispatchScreen.kt
│   │   ├── EodSummaryScreen.kt
│   │   ├── InventoryScreen.kt
│   │   ├── OrderDetailScreen.kt
│   │   └── OrderListScreen.kt
│   └── theme/               # Color, Type, and Material Theme Definitions
└── MainActivity.kt          # Main Entry Point & Edge-to-Edge Setup
```

### Technology Stack

- Language: Kotlin 2.0+
- UI Framework: Jetpack Compose with Material Design 3
- Navigation: Jetpack Navigation Compose
- Architecture Pattern: Clean Architecture (Data, Domain, Presentation) with MVVM & Unidirectional Data Flow (UDF)
- State Management: Kotlin Coroutines, StateFlow, ViewModel
- Local Database: Room Database (SQLite abstraction)
- Networking: Retrofit 2, OkHttp 4
- AI Integration: Google Gemini API (Generative AI SDK) for context-aware operational reporting

### Key Architectural Methods and Rationale

1. Clean Architecture (UI, Domain, Data Layers)
   - Method: Separation of concerns into UI (Compose), Domain (UseCases, Models), and Data (Repositories, Room, Retrofit).
   - Rationale: Decouples business logic from framework-specific APIs, ensuring testability and allowing data sources to be swapped without affecting UI rendering.

2. Unidirectional Data Flow (UDF) with StateFlow
   - Method: ViewModels expose immutable `StateFlow` states to Compose screens. User actions trigger events sent to the ViewModel.
   - Rationale: Eliminates UI state synchronization bugs, race conditions, and accidental double-submits during warehouse operations.

3. Offline-First Caching via Room Database
   - Method: Local SQLite storage via Room DB acting as the source of truth for read operations.
   - Rationale: Factory floors and multi-warehouse facilities frequently suffer from poor network coverage. Offline caching ensures uninterrupted worker picking and loading operations.

4. Real-Time Barcode & QR Code Verification Engine
   - Method: Match scanned SKU barcodes against exact order line items during truck loading.
   - Rationale: Automatically blocks wrong SKU loading (e.g., loading M8 washers instead of M10 washers), reducing return shipment rates and catalog discrepancies.

5. AI Operations Assistant Interface
   - Method: Conversational UI linked with live operational data context injection.
   - Rationale: Allows factory managers to retrieve stock balances, open discrepancies, and shipment summaries using natural language queries without constructing complex manual filters.

---

## Backend - Enterprise Service

The backend service is designed as a high-performance Modular Monolith capable of serving multi-tenant factory operations with strict data isolation.

### Directory Structure & Database Migration

```
backend/
├── db/
│   └── migration/
│       └── V1__init_ordexa_schema.sql   # PostgreSQL Relational Schema & Constraints
```

### Database Schema Overview

The database model covers the complete supply chain hierarchy:
- Companies, Factories, Warehouses, Locations
- Users, Roles, Factory Scopes
- Customers, Products, Purchase Orders, Order Items
- Inventory Stock, Inventory Ledger Transactions
- Trucks, Shipments, Shipment Items, Discrepancy Logs

### Technology Stack

- Language & Runtime: Java 21 LTS (Utilizing Virtual Threads / Project Loom)
- Framework: Spring Boot 3.x, Spring Security 6
- Database: PostgreSQL 16+
- Database Migration: Flyway Versioned SQL Scripts
- Containerization: Docker, Docker Compose
- AI Integration Framework: Spring AI with Deterministic Tool Calling

### Key Architectural Methods and Rationale

1. Modular Monolith Pattern
   - Method: Unified Spring Boot 3 application with strict boundary enforcement between domain modules.
   - Rationale: Provides high developer velocity and single-deployment simplicity while maintaining clean isolation boundaries for future microservices extraction if required.

2. Immutable Inventory Ledger Model
   - Method: Stock changes are saved as immutable transactional ledger entries (`RECEIPT`, `RESERVATION`, `PICK`, `DISPATCH`, `DAMAGE`, `ADJUSTMENT`) rather than simple quantity overwrites.
   - Rationale: Strict accountability for physical vs. reserved stock. Stock reservations do not deduct physical stock until actual dispatch verification occurs, preventing unvetted inventory edits.

3. Database Concurrency Locking Strategy
   - Method: Combined usage of optimistic version checking (`@Version`) and pessimistic row-level locking (`SELECT ... FOR UPDATE`).
   - Rationale: Prevents negative available balances and race conditions when multiple warehouse managers try to reserve or pick the same SKU concurrently.

4. Multi-Tenant Data Isolation
   - Method: Tenant context (`company_id`, `factory_id`) passed via JWT and enforced at the database query level via Spring Security interceptors.
   - Rationale: Guarantees strict data isolation between companies and factories, preventing cross-tenant data leaks.

5. Finite State Machine for Logistics Verification
   - Method: Strict state progression (`DRAFT` -> `READY_FOR_ASSIGNMENT` -> `ASSIGNED` -> `LOADING` -> `LOADED` -> `VERIFIED` -> `DISPATCH_APPROVED` -> `DISPATCHED` -> `DELIVERED`).
   - Rationale: Guarantees that no shipment can be dispatched without 100% scan reconciliation, driver assignment, destination route validation, and manager approval.

---

## Future Planned Aspects (Ordexa 2.0 Roadmap)

1. Inter-Factory Stock Transfers
   - Enabling multi-hop logistics tracking and stock transfer requests across disparate factory locations.

2. CameraX & MLKit On-Device Barcode Scanner
   - Integrating native CameraX frame analysis directly into Compose for zero-latency 1D/2D barcode scanning.

3. Background Sync Engine with Conflict Resolution
   - Expanding Room database synchronization with delta-based server sync and vector clock conflict resolution for offline dispatches.

4. Regional Language Localization
   - Adding multi-language support (Hindi, Punjabi, Kannada, etc.) tailored for factory floor operators.

5. Predictive Inventory Demand Forecasting
   - Machine learning algorithms to analyze historical stock consumption and automatically suggest reorder thresholds.

6. On-Premises Edge LLM Integration
   - Supporting lightweight local LLM execution for environments with strict cloud connectivity restrictions.

---

## Setup and Building

### Prerequisites

- Android Studio Ladybug (2024.2.1) or newer
- JDK 17 or JDK 21
- Android SDK 37 (Minimum SDK 24)
- Docker & Docker Compose (for backend database)

### Building the Android App

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/ordexa.git
   cd ordexa
   ```

2. Open the project in Android Studio.

3. Sync Gradle and run on an Android Emulator or physical device:
   ```bash
   ./gradlew assembleDebug
   ```

### Running Backend Migration

1. Start PostgreSQL using Docker Compose:
   ```bash
   docker-compose up -d
   ```
2. Database Flyway migrations in `backend/db/migration/` will automatically initialize the schema on startup.
