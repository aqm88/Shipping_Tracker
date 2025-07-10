package org.example.project.simulator.updates

import org.example.project.shipment.Shipment
import org.example.project.statuses.Delivered

object Deliver: Update {
    override fun apply(targetShipment: Shipment, command: String) {
        targetShipment.status = Delivered
        targetShipment.currentLocation = "Delivered"
    }
}