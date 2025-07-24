package org.example.project.simulator.updates

import org.example.project.shipment.shipmentTypes.Shipment
import org.example.project.statuses.Shipped

object Ship: Update {
    override fun apply(targetShipment: Shipment, command: String) {
        val expectedDeliveryDate: Long = command.split(',')[3].toLong()
        targetShipment.status = Shipped
        targetShipment.expectedDeliveryDateTimestamp = expectedDeliveryDate
    }
}