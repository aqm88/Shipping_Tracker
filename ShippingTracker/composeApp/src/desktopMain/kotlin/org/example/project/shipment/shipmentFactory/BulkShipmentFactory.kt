package org.example.project.shipment.shipmentFactory

import org.example.project.shipment.shipmentTypes.BulkShipment
import org.example.project.shipment.shipmentTypes.Shipment
import org.example.project.statuses.Created

object BulkShipmentFactory: ShipmentFactory {

    override fun createShipment(id: String, time: Long): Shipment {
        return BulkShipment(id = id, status = Created, timeCreated = time)
    }
}