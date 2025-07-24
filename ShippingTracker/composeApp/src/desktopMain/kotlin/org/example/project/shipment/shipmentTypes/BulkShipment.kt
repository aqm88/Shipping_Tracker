package org.example.project.shipment.shipmentTypes

import org.example.project.shipment.observer.ShippingUpdate
import org.example.project.statuses.Delayed
import org.example.project.statuses.Status

class BulkShipment(id: String,
                   status: Status,
                   private val timeCreated: Long,
                   notes: ArrayList<String> = ArrayList<String>(),
                   updateHistory: ArrayList<ShippingUpdate> = ArrayList<ShippingUpdate>(),
                   expectedDeliveryDate: Long? = null,
                   currentLocation: String? = null): Shipment(id,status,notes,updateHistory,expectedDeliveryDate,currentLocation)  {

    override var expectedDeliveryDateTimestamp: Long? = expectedDeliveryDate
        set(newVal){
            field = newVal
            if (status != Delayed && newVal != null && newVal < earliestDeliveryDate){
                addNote("A bulk shipment was updated to have a delivery date earlier than 3 days after order was placed.")
            }
        }
    private val threeDaysInMilliseconds: Long = 259200000
    private val earliestDeliveryDate = timeCreated + threeDaysInMilliseconds

}