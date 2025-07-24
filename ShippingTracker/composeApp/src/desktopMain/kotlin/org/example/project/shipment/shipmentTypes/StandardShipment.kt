package org.example.project.shipment.shipmentTypes

import org.example.project.shipment.observer.ShippingUpdate
import org.example.project.statuses.Status

class StandardShipment(id: String,
                       status: Status,
                       notes: ArrayList<String> = ArrayList<String>(),
                       updateHistory: ArrayList<ShippingUpdate> = ArrayList<ShippingUpdate>(),
                       expectedDeliveryDateTimestamp: Long? = null,
                       currentLocation: String? = null): Shipment(id,status,notes,updateHistory,expectedDeliveryDateTimestamp,currentLocation)