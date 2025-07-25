package org.example.project.shipment.shipmentFactory

import org.example.project.shipment.shipmentTypes.Shipment
import org.example.project.shipment.shipmentTypes.StandardShipment
import org.example.project.statuses.Created

object StandardShipmentFactory: ShipmentFactory {

    override fun createShipment(id: String, time: Long): Shipment {
        return StandardShipment(id = id, status = Created)

    }
}