package org.example.project.shipment.shipmentFactory

import org.example.project.shipment.shipmentTypes.ExpressShipment
import org.example.project.shipment.shipmentTypes.Shipment
import org.example.project.statuses.Created

object ExpressShipmentFactory: ShipmentFactory {
    override fun createShipment(id: String, time: Long): Shipment {
        return ExpressShipment(id = id, status = Created, timeCreated = time)
    }
}