package org.example.project.simulator.updates

import org.example.project.shipment.shipmentTypes.Shipment

object AddNote: Update {
    override fun apply(targetShipment: Shipment, command: String) {
        val newNote: String = command.split(',')[3]
        targetShipment.addNote(newNote)
    }
}