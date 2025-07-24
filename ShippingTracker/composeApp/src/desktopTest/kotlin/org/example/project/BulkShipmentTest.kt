package org.example.project

import org.example.project.shipment.shipmentTypes.BulkShipment
import org.example.project.statuses.Created
import org.example.project.statuses.Delayed
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNull

class BulkShipmentTest {

    @Test
    fun testBulkShipmentCreation() {
        val shipment = BulkShipment(
            id = "BULK1",
            status = Created,
            timeCreated = 1000L
        )
        assertEquals("BULK1", shipment.id)
        assertEquals("Created", shipment.status.get())
        assertTrue(shipment.notes.isEmpty())
        assertTrue(shipment.updateHistory.isEmpty())
        assertNull(shipment.expectedDeliveryDateTimestamp)
        assertNull(shipment.currentLocation)
    }

    @Test
    fun testExpectedDeliveryDateNoteAddedIfTooEarly() {
        val timeCreated = 1000L
        val earliestAllowed = timeCreated + 259200000 // 3 days in ms
        val shipment = BulkShipment(
            id = "BULK2",
            status = Created,
            timeCreated = timeCreated
        )
        shipment.expectedDeliveryDateTimestamp = earliestAllowed - 1000 // too early
        assertTrue(shipment.notes.any { it.contains("delivery date earlier than 3 days") })
    }

    @Test
    fun testExpectedDeliveryDateNoNoteIfDelayedOrValid() {
        val timeCreated = 2000L
        val earliestAllowed = timeCreated + 259200000
        val shipment = BulkShipment(
            id = "BULK3",
            status = Delayed,
            timeCreated = timeCreated
        )
        shipment.expectedDeliveryDateTimestamp = earliestAllowed - 1000 // too early, but status is Delayed
        assertTrue(shipment.notes.isEmpty())

        shipment.expectedDeliveryDateTimestamp = earliestAllowed + 1000 // valid date
        assertTrue(shipment.notes.isEmpty())
    }

    @Test
    fun testExpectedDeliveryDateSetToNull() {
        val shipment = BulkShipment(
            id = "BULK4",
            status = Created,
            timeCreated = 3000L
        )
        shipment.expectedDeliveryDateTimestamp = null
        assertNull(shipment.expectedDeliveryDateTimestamp)
        assertTrue(shipment.notes.isEmpty())
    }

    @Test
    fun testExpectedDeliveryDateValidNotDelayed() {
        val timeCreated = 4000L
        val earliestAllowed = timeCreated + 259200000
        val shipment = BulkShipment(
            id = "BULK5",
            status = Created,
            timeCreated = timeCreated
        )
        shipment.expectedDeliveryDateTimestamp = earliestAllowed + 1000 // valid date
        assertTrue(shipment.notes.isEmpty())
    }
}