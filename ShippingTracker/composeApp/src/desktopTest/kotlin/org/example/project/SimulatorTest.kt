package org.example.project

import kotlinx.coroutines.runBlocking
import org.example.project.simulator.TrackingSimulator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SimulatorTest {

    @Test
    fun testFullSimulationAndAllUpdateTypes() = runBlocking {

        val simulator = TrackingSimulator()

        // Run simulation
        simulator.runSimulation("test.txt",0)

        // Validate all shipments
        val expectedStatuses = mapOf(
            "s10000" to "Lost",
            "s10001" to "Lost",
            "s10002" to "Lost",
            "s10003" to "Canceled",
            "s10004" to "Canceled",
            "s10005" to "Canceled",
            "s10006" to "Delivered",
            "s10007" to "Delivered",
            "s10008" to "Delivered",
            "s10009" to "Delivered"
        )
        for ((id, status) in expectedStatuses) {
            val shipment = simulator.findShipment(id)
            assertNotNull(shipment, "Shipment $id should exist")
            assertEquals(status, shipment.status.get())
            assertTrue(
                shipment.currentLocation == "Los Angeles CA" || shipment.currentLocation == "Delivered",
                "Location should be either 'Los Angeles CA' or 'Delivered'"
            )
            assertEquals(1652718051403, shipment.expectedDeliveryDateTimestamp)
            assertEquals(3, shipment.notes.size)
        }

        val nonsenseSimulator = TrackingSimulator()
        nonsenseSimulator.runSimulation("nonsense.txt",0)
        assertEquals(null, nonsenseSimulator.findShipment("2"))
    }


    @Test
    fun testFindShipmentReturnsNullForMissingShipment() {
        val simulator = TrackingSimulator()
        assertNull(simulator.findShipment("notfound"))
    }

    @Test
    fun testCreateAndAddShipmentActuallyAddsShipment() {
        val simulator = TrackingSimulator()
        simulator.createAndAddShipment("s12345")
        val shipment = simulator.findShipment("s12345")
        assertNotNull(shipment)
        assertEquals("s12345", shipment.id)
        assertEquals("Created", shipment.status.get())
    }
}