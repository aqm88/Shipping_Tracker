package org.example.project.shipment.observer

interface Observer {
    fun update(shipmentNotes: List<String>,
               shipmentUpdateHistory: List<String>,
               expectedShipmentDeliveryDate: Long?,
               shipmentStatus: String,
               shipmentLocation: String?)
}