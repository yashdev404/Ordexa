package com.example.ordexa.data.repository

import com.example.ordexa.data.mock.MockData
import com.example.ordexa.domain.model.InventoryLedger
import com.example.ordexa.domain.model.InventoryStock
import com.example.ordexa.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InventoryRepository {
    private val _stockList = MutableStateFlow(MockData.sampleInventoryStock)
    val stockList: Flow<List<InventoryStock>> = _stockList.asStateFlow()

    private val _ledgerList = MutableStateFlow(MockData.sampleLedgerTransactions)
    val ledgerList: Flow<List<InventoryLedger>> = _ledgerList.asStateFlow()

    fun recordReceipt(sku: String, qty: Int, grnRef: String, performedBy: String, approvedBy: String): Boolean {
        val currentStock = _stockList.value.find { it.sku == sku } ?: return false
        val newPhysical = currentStock.physicalQty + qty
        val newStock = currentStock.copy(physicalQty = newPhysical)

        _stockList.value = _stockList.value.map { if (it.sku == sku) newStock else it }.toMutableList()

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val newLedger = InventoryLedger(
            id = "STK-000${_ledgerList.value.size + 1}",
            date = today,
            factory = "Factory A",
            warehouse = currentStock.warehouseName,
            sku = sku,
            productName = currentStock.productName,
            transactionType = TransactionType.RECEIPT,
            reference = grnRef,
            qtyIn = qty,
            qtyOut = 0,
            balanceAfter = newPhysical,
            performedBy = performedBy,
            approvedBy = approvedBy,
            notes = "Receipt via GRN $grnRef"
        )
        _ledgerList.value = (listOf(newLedger) + _ledgerList.value).toMutableList()
        return true
    }

    fun recordDamage(sku: String, qty: Int, dmgRef: String, performedBy: String, approvedBy: String, notes: String): Boolean {
        val currentStock = _stockList.value.find { it.sku == sku } ?: return false
        if (currentStock.physicalQty < qty) return false

        val newPhysical = currentStock.physicalQty - qty
        val newStock = currentStock.copy(physicalQty = newPhysical)

        _stockList.value = _stockList.value.map { if (it.sku == sku) newStock else it }.toMutableList()

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val newLedger = InventoryLedger(
            id = "STK-000${_ledgerList.value.size + 1}",
            date = today,
            factory = "Factory A",
            warehouse = currentStock.warehouseName,
            sku = sku,
            productName = currentStock.productName,
            transactionType = TransactionType.DAMAGE,
            reference = dmgRef,
            qtyIn = 0,
            qtyOut = qty,
            balanceAfter = newPhysical,
            performedBy = performedBy,
            approvedBy = approvedBy,
            notes = notes
        )
        _ledgerList.value = (listOf(newLedger) + _ledgerList.value).toMutableList()
        return true
    }
}
