package org.example.project.simulator.updates

import org.example.project.shipment.Shipment
import org.example.project.statuses.Canceled

object Cancel: Update {
    override fun apply(targetShipment: Shipment, command: String) {
        targetShipment.status = Canceled
    }
}