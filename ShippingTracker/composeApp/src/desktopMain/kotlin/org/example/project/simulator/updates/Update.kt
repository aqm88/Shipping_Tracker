package org.example.project.simulator.updates

import org.example.project.shipment.Shipment

interface Update {
    fun apply(targetShipment: Shipment, command: String)
}