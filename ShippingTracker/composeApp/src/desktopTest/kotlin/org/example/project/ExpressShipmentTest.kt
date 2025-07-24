package org.example.project

import org.example.project.shipment.shipmentTypes.ExpressShipment
import org.example.project.statuses.Created
import org.example.project.statuses.Delayed
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNull

class ExpressShipmentTest {

    @Test
    fun testExpressShipmentCreation() {
        val shipment = ExpressShipment(
            id = "EXP1",
            status = Created,
            timeCreated = 1000L
        )
        assertEquals("EXP1", shipment.id)
        assertEquals("Created", shipment.status.get())
        assertTrue(shipment.notes.isEmpty())
        assertTrue(shipment.updateHistory.isEmpty())
        assertNull(shipment.expectedDeliveryDateTimestamp)
        assertNull(shipment.currentLocation)
    }

    @Test
    fun testExpectedDeliveryDateNoteAddedIfTooLate() {
        val timeCreated = 2000L
        val latestAllowed = timeCreated + 259200000 // 3 days in ms
        val shipment = ExpressShipment(
            id = "EXP2",
            status = Created,
            timeCreated = timeCreated
        )
        shipment.expectedDeliveryDateTimestamp = latestAllowed + 1000 // too late
        assertTrue(shipment.notes.any { it.contains("delivery date later than 3 days") })
    }

    @Test
    fun testExpectedDeliveryDateNoNoteIfDelayedOrValid() {
        val timeCreated = 3000L
        val latestAllowed = timeCreated + 259200000
        val shipment = ExpressShipment(
            id = "EXP3",
            status = Delayed,
            timeCreated = timeCreated
        )
        shipment.expectedDeliveryDateTimestamp = latestAllowed + 1000 // too late, but status is Delayed
        assertTrue(shipment.notes.isEmpty())

        shipment.expectedDeliveryDateTimestamp = latestAllowed - 1000 // valid date
        assertTrue(shipment.notes.isEmpty())
    }

    @Test
    fun testExpectedDeliveryDateSetToNull() {
        val shipment = ExpressShipment(
            id = "EXP4",
            status = Created,
            timeCreated = 4000L
        )
        shipment.expectedDeliveryDateTimestamp = null
        assertNull(shipment.expectedDeliveryDateTimestamp)
        assertTrue(shipment.notes.isEmpty())
    }

    @Test
    fun testExpectedDeliveryDateNotTooLate() {
        val timeCreated = 5000L
        val latestAllowed = timeCreated + 259200000
        val shipment = ExpressShipment(
            id = "EXP5",
            status = Created,
            timeCreated = timeCreated
        )
        shipment.expectedDeliveryDateTimestamp = latestAllowed // exactly at the limit
        assertTrue(shipment.notes.isEmpty())

        shipment.expectedDeliveryDateTimestamp = latestAllowed - 1000 // before the limit
        assertTrue(shipment.notes.isEmpty())
    }
}