package org.example.project.shipment.shipmentTypes

import org.example.project.shipment.observer.Observer
import org.example.project.shipment.observer.ShippingUpdate
import org.example.project.shipment.observer.Subject
import org.example.project.statuses.Status

abstract class Shipment(
    val id: String,
    status: Status,
    notes: ArrayList<String> = ArrayList<String>(),
    updateHistory: ArrayList<ShippingUpdate> = ArrayList<ShippingUpdate>(),
    expectedDeliveryDateTimestamp: Long? = null,
    currentLocation: String? = null): Subject {

    open var expectedDeliveryDateTimestamp: Long? = expectedDeliveryDateTimestamp
        set(value) {
            field = value
            notifyObservers()
        }
    private val observers = ArrayList<Observer>()
    var status = status
        set(value) {
            val oldStatus = field
            field = value
            addUpdate(ShippingUpdate(oldStatus, value, System.currentTimeMillis()))
        }

    var notes: ArrayList<String> = notes
        private set
    var updateHistory: ArrayList<ShippingUpdate> = updateHistory
        private set

    var currentLocation: String? = currentLocation
        set(value) {
            field = value
            notifyObservers()
        }


    override fun registerObserver(observer: Observer) {
        this.observers.add(observer)
    }

    override fun removeObserver(observer: Observer) {
        this.observers.remove(observer)
    }

    override fun notifyObservers() {
        val updateHistoryStrings: List<String> = updateHistory.map { it.toString() }
        for (obs in observers) {
            obs.update(notes.toList(), updateHistoryStrings, expectedDeliveryDateTimestamp, status.get(), currentLocation )
        }
    }

    private fun addUpdate(update: ShippingUpdate) {
        updateHistory.add(update)
        notifyObservers()
    }

    fun addNote(note: String) {
        notes = ArrayList(notes).apply { add(note) }
        notifyObservers()
    }

}