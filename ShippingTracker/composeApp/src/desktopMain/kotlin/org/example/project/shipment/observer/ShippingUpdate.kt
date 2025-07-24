package org.example.project.shipment.observer
import org.example.project.statuses.Status
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class ShippingUpdate(
    private val previousStatus: Status,
    private val newStatus: Status,
    private val timestamp: Long) {

    private var formattedDate: String

    init {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault())
        formattedDate = formatter.format(Instant.ofEpochMilli(timestamp))
    }

    override fun toString(): String{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault())
        val formattedDate = formatter.format(Instant.ofEpochMilli(timestamp))
        return "Shipment went from ${previousStatus.get()} to ${newStatus.get()} on $formattedDate"
    }
}