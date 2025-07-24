package org.example.project.shipment.shipmentTypes

import org.example.project.shipment.ShippingUpdate
import org.example.project.statuses.Delayed
import org.example.project.statuses.Status

class OvernightShipment(id: String,
                        status: Status,
                        private val timeCreated: Long,
                        notes: ArrayList<String> = ArrayList<String>(),
                        updateHistory: ArrayList<ShippingUpdate> = ArrayList<ShippingUpdate>(),
                        expectedDeliveryDate: Long? = null,
                        currentLocation: String? = null): Shipment(id,status,notes,updateHistory,expectedDeliveryDate,currentLocation) {

    override var expectedDeliveryDateTimestamp: Long? = expectedDeliveryDate
        set(newVal){
            field = newVal
            if (status != Delayed && newVal != null && newVal > latestDeliveryDate){
                addNote("An overnight shipment was updated to have a delivery date later than the day after order was placed.")
            }
        }
    private val dayInMilliseconds: Long = 86400000
    private val latestDeliveryDate = timeCreated + dayInMilliseconds
}