package org.example.project.simulator.updates

import org.example.project.shipment.shipmentTypes.Shipment

interface Update {
    fun apply(targetShipment: Shipment, command: String)
}