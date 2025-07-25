package org.example.project.updates

import org.example.project.shipment.shipmentTypes.Shipment

interface Update {
    fun apply(targetShipment: Shipment, command: String)
}