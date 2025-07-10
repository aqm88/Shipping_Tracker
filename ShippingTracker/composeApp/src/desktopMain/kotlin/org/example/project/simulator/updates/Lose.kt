package org.example.project.simulator.updates

import org.example.project.shipment.Shipment
import org.example.project.statuses.Lost

object Lose: Update {
    override fun apply(targetShipment: Shipment, command: String) {
        targetShipment.status = Lost
    }
}