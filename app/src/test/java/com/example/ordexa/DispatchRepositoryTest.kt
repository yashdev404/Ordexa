package com.example.ordexa

import com.example.ordexa.data.repository.DispatchRepository
import com.example.ordexa.domain.model.Truck
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DispatchRepositoryTest {

    private lateinit var dispatchRepository: DispatchRepository

    @Before
    fun setUp() {
        dispatchRepository = DispatchRepository()
    }

    @Test
    fun `test truck route mismatch triggers exception review and discrepancy log`() = runBlocking {
        val shipmentId = "SHP-2041" // Destination: Delhi
        val mismatchedTruck = Truck(
            id = "TRK-PB",
            vehicleNumber = "PB-65-TEST",
            driverName = "Driver A",
            driverPhone = "99999",
            routeName = "Chandigarh Route", // Mismatched route!
            capacityKg = 5000.0
        )

        val errorMsg = dispatchRepository.assignTruckToShipment(shipmentId, mismatchedTruck)
        assertNotNull("Error message should be returned for route mismatch", errorMsg)
        assertTrue("Error message should indicate Route Mismatch", errorMsg!!.contains("Route Mismatch"))

        val latestDiscrepancy = dispatchRepository.discrepancies.first().firstOrNull()
        assertNotNull(latestDiscrepancy)
        assertEquals("DESTINATION_MISMATCH", latestDiscrepancy?.discrepancyType)
    }
}
