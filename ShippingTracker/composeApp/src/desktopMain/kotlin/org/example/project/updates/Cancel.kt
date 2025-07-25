package org.example.project.updates

import org.example.project.shipment.shipmentTypes.Shipment
import org.example.project.statuses.Canceled

object Cancel: Update {
    override fun apply(targetShipment: Shipment, command: String) {
        targetShipment.status = Canceled
    }
}