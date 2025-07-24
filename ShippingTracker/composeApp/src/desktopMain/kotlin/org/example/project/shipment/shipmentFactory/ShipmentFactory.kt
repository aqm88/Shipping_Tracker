package org.example.project.shipment.shipmentFactory

import org.example.project.shipment.shipmentTypes.Shipment

interface ShipmentFactory {

    abstract fun createShipment(id: String, time: Long): Shipment

}