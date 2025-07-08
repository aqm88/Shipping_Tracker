package org.example.project.shipment

class ShipmentObserver(shipmentId: String,
                       shipmentNotes: List<String>,
                       shipmentUpdateHistory: List<String>,
                       expectedShipmentDeliveryDate: Long?,
                       shipmentStatus: String,
                       shipmentLocation: String?): Observer {

    var shipmentId: String = shipmentId
        private set
    var shipmentNotes: List<String> = shipmentNotes
        private set
    var shipmentUpdateHistory: List<String> = shipmentUpdateHistory
        private set
    var expectedShipmentDeliveryDate: Long? = expectedShipmentDeliveryDate
        private set
    var shipmentStatus: String = shipmentStatus
        private set
    var shipmentLocation: String? = shipmentLocation
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