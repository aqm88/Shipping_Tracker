package org.example.project

import org.example.project.shipment.shipmentFactory.BulkShipmentFactory
import org.example.project.shipment.shipmentFactory.ExpressShipmentFactory
import org.example.project.shipment.shipmentFactory.OvernightShipmentFactory
import org.example.project.shipment.shipmentFactory.StandardShipmentFactory
import org.example.project.shipment.shipmentTypes.BulkShipment
import org.example.project.shipment.shipmentTypes.ExpressShipment
import org.example.project.shipment.shipmentTypes.OvernightShipment
import org.example.project.shipment.shipmentTypes.StandardShipment
import org.example.project.statuses.Created
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class FactoriesTest {

    @Test
    fun testStandardShipmentFactory() {
        val factory = StandardShipmentFactory()
        val shipment = factory.createShipment("STD1", 1000L)
        assertTrue(shipment is StandardShipment)
        assertEquals("STD1", shipment.id)
        assertEquals("Created", shipment.status.get())
    }

    @Test
    fun testExpressShipmentFactory() {
        val factory = ExpressShipmentFactory()
        val shipment = factory.createShipment("EXP1", 2000L)
        assertTrue(shipment is ExpressShipment)
        assertEquals("EXP1", shipment.id)
        assertEquals("Created", shipment.status.get())
    }

    @Test
    fun testOvernightShipmentFactory() {
        val factory = OvernightShipmentFactory()
        val shipment = factory.createShipment("ON1", 3000L)
        assertTrue(shipment is OvernightShipment)
        assertEquals("ON1", shipment.id)
        assertEquals("Created", shipment.status.get())
    }

    @Test
    fun testBulkShipmentFactory() {
        val factory = BulkShipmentFactory()
        val shipment = factory.createShipment("BLK1", 4000L)
        assertTrue(shipment is BulkShipment)
        assertEquals("BLK1", shipment.id)
        assertEquals("Created", shipment.status.get())
    }
}