package org.example.project.simulator.updates

import org.example.project.shipment.Shipment
import org.example.project.statuses.Delayed

object Delay: Update {
    override fun apply(targetShipment: Shipment, command: String) {
        val newDeliveryDate: Long = command.split(',')[3].toLong()
        targetShipment.expectedDeliveryDateTimestamp = newDeliveryDate
        targetShipment.status = Delayed
    }
}