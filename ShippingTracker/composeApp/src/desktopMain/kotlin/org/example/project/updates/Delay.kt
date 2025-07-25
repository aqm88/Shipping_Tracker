package org.example.project.updates

import org.example.project.shipment.shipmentTypes.Shipment
import org.example.project.statuses.Delayed

object Delay: Update {
    override fun apply(targetShipment: Shipment, command: String) {
        val newDeliveryDate: Long = command.split(',')[3].toLong()
        targetShipment.status = Delayed
        targetShipment.expectedDeliveryDateTimestamp = newDeliveryDate
    }
}