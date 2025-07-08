package org.example.project.shipment

interface Observer {
    fun update(shipmentNotes: List<String>,
               shipmentUpdateHistory: List<String>,
               expectedShipmentDeliveryDate: Long?,
               shipmentStatus: String,
               shipmentLocation: String?)
}