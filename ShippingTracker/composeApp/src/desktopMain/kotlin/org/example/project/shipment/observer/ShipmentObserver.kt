package org.example.project.shipment.observer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ShipmentObserver(
    shipmentId: String,
    shipmentNotes: List<String>,
    shipmentUpdateHistory: List<String>,
    expectedShipmentDeliveryDate: Long?,
    shipmentStatus: String,
    shipmentLocation: String?
) : Observer {

    var shipmentId: String = shipmentId
        private set

    var shipmentNotes by mutableStateOf(shipmentNotes)
        private set

    var shipmentUpdateHistory by mutableStateOf(shipmentUpdateHistory)
        private set

    var expectedShipmentDeliveryDate by mutableStateOf(expectedShipmentDeliveryDate)
        private set

    var shipmentStatus by mutableStateOf(shipmentStatus)
        private set

    var shipmentLocation by mutableStateOf(shipmentLocation)
        private set

    override fun update(
        shipmentNotes: List<String>,
        shipmentUpdateHistory: List<String>,
        expectedShipmentDeliveryDate: Long?,
        shipmentStatus: String,
        shipmentLocation: String?
    ) {
        this.shipmentNotes = shipmentNotes
        this.shipmentUpdateHistory = shipmentUpdateHistory
        this.expectedShipmentDeliveryDate = expectedShipmentDeliveryDate
        this.shipmentStatus = shipmentStatus
        this.shipmentLocation = shipmentLocation
    }
}