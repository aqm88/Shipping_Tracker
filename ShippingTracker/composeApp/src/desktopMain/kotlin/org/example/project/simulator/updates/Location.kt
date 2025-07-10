package org.example.project.simulator.updates

import org.example.project.shipment.Shipment

object Location: Update {
    override fun apply(targetShipment: Shipment, command: String){
        val newLocation: String = command.split(',')[3]
        targetShipment.currentLocation = newLocation
    }
}