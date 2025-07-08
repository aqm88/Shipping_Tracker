package org.example.project

import junit.framework.TestCase.assertTrue
import org.example.project.shipment.Shipment
import org.example.project.shipment.ShipmentObserver
import org.example.project.shipment.ShippingUpdate
import org.example.project.statuses.Canceled
import org.example.project.statuses.Created
import org.example.project.statuses.Delayed
import org.example.project.statuses.Delivered
import org.example.project.statuses.Lost
import org.example.project.statuses.Shipped
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
class ShipmentTest {


    @Test
    fun testShipmentCreation(){
        val shipment = Shipment(
            id = "12345",
            status = Created,
            notes = ArrayList<String>(),
            updateHistory = ArrayList<ShippingUpdate>(),
            expectedDeliveryDateTimestamp = null,
            currentLocation = null
        )

        assertEquals("12345", shipment.id)
    }

    @Test
    fun testShipmentUpdate(){
        val shipment = Shipment(
            id = "12345",
            status = Created,
            notes = ArrayList<String>(),
            updateHistory = ArrayList<ShippingUpdate>(),
            expectedDeliveryDateTimestamp = null,
            currentLocation = null
        )

        shipment.status = Shipped

        assertEquals(1, shipment.updateHistory.size)


    }

    @Test
    fun testNotifyObservers(){
        val shipment = Shipment(
            id = "12345",
            status = Created,
            notes = ArrayList<String>(),
            updateHistory = ArrayList<ShippingUpdate>(),
            expectedDeliveryDateTimestamp = null,
            currentLocation = null
        )

        val shipmentObserver = ShipmentObserver(
            shipment.id,
            shipment.notes,
            shipment.updateHistory.map { it.toString() },
            shipment.expectedDeliveryDateTimestamp,
            shipment.status.get(),
            shipment.currentLocation
        )

        assertEquals("Created",shipmentObserver.shipmentStatus)

        shipment.registerObserver(shipmentObserver)

        shipment.status = Shipped
        shipment.addNote("Shipment has been shipped.")
        shipment.currentLocation = "LA"

        assertEquals("Shipped", shipmentObserver.shipmentStatus)

        shipment.removeObserver(shipmentObserver)

        shipment.status = Created

        assertEquals("Created", shipment.status.get())
        assertEquals("Shipped", shipmentObserver.shipmentStatus)
    }

    @Test
    fun testConstructorWithAllParams() {
        val shipment = Shipment(
            id = "A1",
            status = Created,
            notes = arrayListOf("Initial note"),
            updateHistory = arrayListOf(ShippingUpdate(Created, Shipped, 1L)),
            expectedDeliveryDateTimestamp = 123456789L,
            currentLocation = "Origin"
        )
        assertEquals("A1", shipment.id)
        assertEquals("Created", shipment.status.get())
        assertEquals(1, shipment.notes.size)
        assertEquals(1, shipment.updateHistory.size)
        assertEquals(123456789L, shipment.expectedDeliveryDateTimestamp)
        assertEquals("Origin", shipment.currentLocation)
    }

    @Test
    fun testConstructorWithDefaults() {
        val shipment = Shipment(
            id = "B2",
            status = Shipped
        )
        assertEquals("B2", shipment.id)
        assertEquals("Shipped", shipment.status.get())
        assertTrue(shipment.notes.isEmpty())
        assertTrue(shipment.updateHistory.isEmpty())
        assertNull(shipment.expectedDeliveryDateTimestamp)
        assertNull(shipment.currentLocation)
    }

    @Test
    fun testAllStatusTypes() {
        val statuses = listOf(Created, Shipped /*, Delivered, Cancelled, etc.*/)
        for (status in statuses) {
            val shipment = Shipment("C3", status)
            assertEquals(status.get(), shipment.status.get())
        }
    }

    @Test
    fun testEmptyNotesAndHistory() {
        val shipment = Shipment("D4", Created, ArrayList(), ArrayList())
        assertTrue(shipment.notes.isEmpty())
        assertTrue(shipment.updateHistory.isEmpty())
    }

    @Test
    fun testNullExpectedDeliveryDate() {
        val shipment = Shipment("E5", Created, expectedDeliveryDateTimestamp = null)
        assertNull(shipment.expectedDeliveryDateTimestamp)
    }

    @Test
    fun testStatusReassignmentToSameValue() {
        val shipment = Shipment("G7", Created)
        shipment.status = Created
        assertEquals(1, shipment.updateHistory.size)
        shipment.status = Created
        assertEquals(2, shipment.updateHistory.size)
    }

    @Test
    fun testStatuses(){
        val created = Created.get()
        assertEquals("Created", created)
        val shipped = Shipped.get()
        assertEquals("Shipped", shipped)
        val delivered = Delivered.get()
        assertEquals("Delivered", delivered)
        val cancelled = Canceled.get()
        assertEquals("Canceled", cancelled)
        val delayed = Delayed.get()
        assertEquals("Delayed", delayed)
        val lost = Lost.get()
        assertEquals("Lost", lost)
    }

}