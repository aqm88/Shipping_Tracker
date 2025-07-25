package org.example.project.shipment.observer

interface Subject {

    fun registerObserver(observer: Observer)
    fun removeObserver(observer: Observer)
    fun notifyObservers()
}