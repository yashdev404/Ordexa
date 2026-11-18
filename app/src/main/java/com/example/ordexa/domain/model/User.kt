package com.example.ordexa.domain.model

enum class UserRole(val displayName: String, val scope: String) {
    SUPER_ADMIN("Super Admin", "Company"),
    OPERATIONS_HEAD("Operations Head", "Company"),
    FACTORY_MANAGER("Factory Manager", "Factory"),
    WAREHOUSE_MANAGER("Warehouse Manager", "Warehouse"),
    DISPATCH_MANAGER("Dispatch Manager", "Logistics"),
    FIELD_MANAGER("Field Manager", "Operations"),
    WORKER("Worker", "Assigned Tasks")
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val companyId: String,
    val permittedFactoryIds: List<String> = emptyList()
)

data class Factory(
    val id: String,
    val name: String,
    val code: String,
    val location: String
)

data class Warehouse(
    val id: String,
    val factoryId: String,
    val name: String,
    val code: String
)
