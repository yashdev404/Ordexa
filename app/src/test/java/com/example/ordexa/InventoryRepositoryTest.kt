package com.example.ordexa

import com.example.ordexa.data.repository.InventoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class InventoryRepositoryTest {

    private lateinit var inventoryRepository: InventoryRepository

    @Before
    fun setUp() {
        inventoryRepository = InventoryRepository()
    }

    @Test
    fun `test GRN receipt increases physical stock and logs transaction`() = runBlocking {
        val sku = "AX-100"
        val receiptQty = 500
        val grn = "GRN-TEST-100"

        val initialStock = inventoryRepository.stockList.first().find { it.sku == sku }
        assertNotNull(initialStock)
        val initialPhysical = initialStock!!.physicalQty

        val success = inventoryRepository.recordReceipt(sku, receiptQty, grn, "Tester", "Manager")
        assertTrue("Receipt recording should succeed", success)

        val updatedStock = inventoryRepository.stockList.first().find { it.sku == sku }
        assertEquals(initialPhysical + receiptQty, updatedStock?.physicalQty)

        val latestLedger = inventoryRepository.ledgerList.first().firstOrNull()
        assertNotNull(latestLedger)
        assertEquals(grn, latestLedger?.reference)
    }

    @Test
    fun `test damaged goods reduces physical stock and requires approval`() = runBlocking {
        val sku = "AX-220"
        val damageQty = 50
        val dmgRef = "DMG-TEST-200"

        val initialStock = inventoryRepository.stockList.first().find { it.sku == sku }
        assertNotNull(initialStock)
        val initialPhysical = initialStock!!.physicalQty

        val success = inventoryRepository.recordDamage(sku, damageQty, dmgRef, "Worker A", "Manager B", "Crushed package")
        assertTrue("Damage report should succeed", success)

        val updatedStock = inventoryRepository.stockList.first().find { it.sku == sku }
        assertEquals(initialPhysical - damageQty, updatedStock?.physicalQty)
    }
}
