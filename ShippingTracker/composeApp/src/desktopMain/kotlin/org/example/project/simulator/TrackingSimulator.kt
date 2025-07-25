package org.example.project.simulator

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.example.project.shipment.shipmentTypes.Shipment
import org.example.project.shipment.shipmentTypes.StandardShipment
import org.example.project.updates.updateType
import org.example.project.statuses.Created
import java.io.File

class TrackingSimulator {
    
    private val shipments = ArrayList<Shipment>()
    
    fun findShipment(id: String): Shipment? {
        return shipments.find { it.id == id }
    }
    fun createAndAddShipment(id: String) {
        val newShipment = StandardShipment(
            id = id,
            status = Created
        )
        shipments.add(newShipment)
    }
    fun runSimulation(simulationFile: String, delayMillis: Long = 1000) = runBlocking{
        val lines = File(simulationFile).readLines()
        for(line in lines){
            val commandType = line.split(",")[0]
            if(commandType == "created"){
                val id = line.split(",")[1]
                createAndAddShipment(id)
            }else{
                val targetShipment = findShipment(line.split(",")[1])?: continue
                updateType[commandType]?.apply(targetShipment, line)
            }
            delay(delayMillis)
        }
    }
}