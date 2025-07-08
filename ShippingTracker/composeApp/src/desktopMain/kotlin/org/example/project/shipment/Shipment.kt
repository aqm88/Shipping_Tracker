package org.example.project.shipment

import org.example.project.statuses.Status

class Shipment(
    val id: String,
    status: Status,
    notes: ArrayList<String> = ArrayList<String>(),
    updateHistory: ArrayList<ShippingUpdate> = ArrayList<ShippingUpdate>(),
    var expectedDeliveryDateTimestamp: Long? = null,
    currentLocation: String? = null): Subject {

    private val observers = ArrayList<Observer>()
    var status = status
        set(value) {
            field = value
            addUpdate(ShippingUpdate(field, value, System.currentTimeMillis()))
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
            obs.update(notes, updateHistoryStrings, expectedDeliveryDateTimestamp, status.get(), currentLocation )
        }
    }

    private fun addUpdate(update: ShippingUpdate) {
        updateHistory.add(update)
        notifyObservers()
    }

    fun addNote(note: String) {
        notes.add(note)
        notifyObservers()
    }

}