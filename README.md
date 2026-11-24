# Ordexa - Intelligent Factory Operations, Inventory, and Dispatch Management System

Ordexa is an enterprise-grade factory operations, multi-warehouse inventory, and dispatch management platform. The platform is designed around three core operational principles:

1. **Every order must be traceable.**
2. **Every stock movement must be accountable.**
3. **Every shipment must be verifiable.**

---

## System Architecture Overview

Ordexa operates on a Client-Server modular architecture. The Android client communicates with the Spring Boot backend service via REST APIs over HTTP/JSON, managing local offline persistence and real-time operational workflows.

```
+-------------------------------------------------------------------+
|                        Ordexa Mobile (Android)                    |
|          Kotlin + Jetpack Compose + Room + StateFlow + Retrofit   |
+-------------------------------------------------------------------+
                                  |
                                  | REST API (HTTP / JSON)
                                  v
+-------------------------------------------------------------------+
|                     Ordexa Backend (Spring Boot 3.4)              |
|                                                                   |
|  +---------------+  +---------------+  +-----------------------+  |
|  | Security & Auth|  | Factory & Org |  | Order Management      |  |
|  | Module (JWT)  |  | Module        |  | Module (PDF/State)    |  |
|  +---------------+  +---------------+  +-----------------------+  |
|  +---------------+  +---------------+  +-----------------------+  |
|  | Inventory     |  | Dispatch &    |  | Product Management    |  |
|  | Ledger Module |  | Logistics     |  | Module (CRUD / SKU)   |  |
|  +---------------+  +---------------+  +-----------------------+  |
+-------------------------------------------------------------------+
                                  |
               +------------------+------------------+
               |                                     |
               v                                     v
   +-----------------------+             +-----------------------+
   | PostgreSQL 16 DB      |             | Adminer Web GUI       |
   | (Flyway Migrations)   |             | (Port 8081)           |
   +-----------------------+             +-----------------------+
```

---

## Frontend - Android Application

The frontend is a native Android application built using modern Android development practices, declarative UI, and Clean Architecture.

### Directory Structure

```
app/src/main/java/com/example/ordexa/
├── data/
│   ├── local/
│   │   └── entities/        # Room Database Entities (DispatchEntity, InventoryEntity, OrderEntity)
│   ├── mock/                # Mock Data for Off-Network Testing & Demos
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

- **Language:** Kotlin 2.0+
- **UI Framework:** Jetpack Compose with Material Design 3
- **Navigation:** Jetpack Navigation Compose
- **Architecture Pattern:** Clean Architecture (Data, Domain, Presentation) with MVVM & Unidirectional Data Flow (UDF)
- **State Management:** Kotlin Coroutines, StateFlow, ViewModel
- **Local Database:** Room Database (SQLite abstraction)
- **Build System:** Gradle 9.5+ with AGP 9.0+

---

## Backend - Enterprise Service

The backend service is designed as a high-performance Modular Monolith capable of serving multi-tenant factory operations with strict data isolation.

### Directory Structure

```
backend/
├── src/main/java/com/ordexa/backend/
│   ├── config/              # SecurityConfig (Stateless CORS & BCrypt)
│   ├── controller/          # REST Controllers
│   │   ├── AuthController.java
│   │   ├── DispatchController.java
│   │   ├── EodController.java
│   │   ├── InventoryController.java
│   │   ├── OrderController.java
│   │   └── ProductController.java       # Product Management APIs
│   ├── dto/                 # Request & Response Data Transfer Objects
│   ├── entity/              # JPA Entities (PostgreSQL Tables)
│   ├── repository/          # Spring Data JPA Repositories
│   ├── service/             # Business Services & Validation Logic
│   └── OrdexaApplication.java
└── src/main/resources/
    ├── application.yml      # Environment-variable ready configuration
    └── db/migration/        # Flyway SQL migrations
        ├── V1__init_ordexa_schema.sql
        └── V2__seed_ordexa_data.sql
```

### Technology Stack

- **Language & Runtime:** Java 21 LTS
- **Framework:** Spring Boot 3.4.0, Spring Security 6, Spring Data JPA
- **Database:** PostgreSQL 16+ (Dockerized container)
- **Database Migration:** Flyway 10+ (`flyway-core`, `flyway-database-postgresql`)
- **Containerization:** Docker & Docker Compose
- **Build System:** Gradle 9.5 (Multi-Project integration with `:app` and `:backend`)

### Available API Endpoints (Base URL: `/api/v1`)

| Module | Method | Endpoint | Description |
| :--- | :--- | :--- | :--- |
| **Auth** | `POST` | `/api/v1/auth/login` | User login and JWT authentication |
| **Products** | `POST` | `/api/v1/products` | Create a new product (SKU, name, UOM, unit price) |
| | `GET` | `/api/v1/products?companyId={id}` | List all products belonging to a company |
| | `GET` | `/api/v1/products/{id}` | Retrieve single product details by ID |
| | `GET` | `/api/v1/products/by-sku?companyId={id}&sku={sku}` | Look up product by company ID and SKU |
| | `PUT` | `/api/v1/products/{id}` | Update product details |
| | `DELETE` | `/api/v1/products/{id}` | Delete product |
| **Orders** | `GET` | `/api/v1/orders?factoryId={id}` | List orders for a specific factory |
| | `POST` | `/api/v1/orders/ingest` | Ingest Purchase Orders (customer, line items, totals) |
| | `PUT` | `/api/v1/orders/{id}/status` | Update order status (`DRAFT`, `APPROVED`, etc.) |
| **Inventory**| `GET` | `/api/v1/inventory/stock?warehouseId={id}` | Get real-time stock levels by warehouse |
| | `POST` | `/api/v1/inventory/grn` | Record Goods Receipt Note (GRN) |
| | `POST` | `/api/v1/inventory/damage` | Report damaged goods and record discrepancy |
| **Dispatch** | `GET` | `/api/v1/dispatch/shipments?factoryId={id}` | View factory shipments |
| | `POST` | `/api/v1/dispatch/assign-truck` | Assign truck and driver to a shipment |
| | `POST` | `/api/v1/dispatch/resolve-discrepancy/{id}` | Resolve dispatch discrepancy with manager sign-off |
| **EOD** | `GET` | `/api/v1/eod/summary?factoryId={id}&date={date}` | Generate daily End-of-Day operations summary |

---

## Setup & Running the Project

### Prerequisites

- **Java JDK 21**
- **Android Studio Ladybug (2024.2.1+)** with Android SDK 37 (Min SDK 24)
- **Docker Desktop** (running with WSL 2 on Windows)

---

### Step 1: Start the Database & Adminer (Docker)

From the project root directory, run:
```powershell
docker compose up -d
```
* **PostgreSQL 16:** Runs on `localhost:5432` (database: `ordexa_db`, user: `ordexa_user`)
* **Adminer Web GUI:** Runs on `http://localhost:8081` for database inspection.

---

### Step 2: Start the Backend Service

In your terminal from the project root:
```powershell
.\gradlew :backend:bootRun
```
* Spring Boot will start on **`http://localhost:8080/api/v1`**.
* Flyway will automatically execute migrations and seed sample ground-truth data.

---

### Step 3: Run the Android App

1. In Android Studio, select **`app`** in the top run configuration dropdown.
2. Select your Android emulator (e.g. `Pixel_7`) or connected physical device.
3. Click the green **Run (▶)** button (or press `Shift + F10`).

> **Note for Emulator Networking:**
> The Android emulator reaches your computer's `localhost` via `10.0.2.2`. To connect the app to the local backend, use `http://10.0.2.2:8080/api/v1`.
