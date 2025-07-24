package org.example.project.shipment.shipmentFactory

import org.example.project.shipment.shipmentTypes.OvernightShipment
import org.example.project.shipment.shipmentTypes.Shipment
import org.example.project.statuses.Created

class OvernightShipmentFactory: ShipmentFactory {

    override fun createShipment(id: String, time: Long): Shipment {
        return OvernightShipment(id = id, status = Created, timeCreated = time)
    }
}