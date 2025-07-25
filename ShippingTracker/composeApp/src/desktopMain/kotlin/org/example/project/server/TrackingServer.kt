package org.example.project.server

import io.ktor.server.application.call
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.respondHtml
import io.ktor.server.netty.Netty
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.html.FormMethod
import kotlinx.html.body
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.submitInput
import kotlinx.html.textInput
import org.example.project.shipment.shipmentFactory.factoryType
import org.example.project.shipment.shipmentTypes.Shipment
import org.example.project.updates.updateType
import kotlinx.html.p

object TrackingServer {
    
    private val shipments = ArrayList<Shipment>()
    private var serverInstance: ApplicationEngine? = null
    fun findShipment(id: String): Shipment? {
        return shipments.find { it.id == id }
    }

    fun runServer(){
        serverInstance = embeddedServer(Netty, port = 8080) {
            routing {
                get("/") {
                    call.respondHtml {
                        body {
                            h1 { +"Tracking Server" }
                            form(action = "/command", method = FormMethod.post) {
                                textInput { name = "command" }
                                submitInput { value = "Submit" }
                            }
                        }
                    }
                }
                post("/command") {
                    val params = call.receiveParameters()
                    val command = params["command"] ?: " "
                    val logMessage = processCommand(command)
                    call.respondHtml {
                        body {
                            h1 { +"Tracking Server" }
                            form(action = "/command", method = FormMethod.post) {
                                textInput { name = "command" }
                                submitInput { value = "Submit" }
                            }
                            p { +"Result: $logMessage" }
                        }
                    }
                }
            }
        }.start(wait = false)
    }

    fun processCommand(command: String): String {
        if (command.isBlank()) {
            return "No command provided"
        }
        val parts = command.split(",")
        val commandType = parts[0].trim()

        if (commandType == "created") {
            if (parts.size < 4) return "Invalid command format for 'created'"
            val id = parts[1].trim()
            val type = parts[2].trim()
            val time = parts[3].trim().toLongOrNull()
            if (time == null) return "Invalid timestamp"
            val newShipment = factoryType[type]?.createShipment(id, time)
            if (newShipment != null) {
                shipments.add(newShipment)
                return "Shipment created: $id"
            } else {
                return "Invalid shipment type: $type"
            }
        } else {
            if (parts.size < 2) return "Invalid update command format"
            val shipmentId = parts[1].trim()
            val targetShipment = findShipment(shipmentId)
            if (targetShipment == null) return "Shipment not found: $shipmentId"
            val updater = updateType[commandType]
            if (updater == null) return "Unknown command type: $commandType"
            updater.apply(targetShipment, command)
            return "Update applied: $commandType to shipment ${targetShipment.id}"
        }
    }

    fun stopServer() {
        serverInstance?.stop(1000, 1000)
        serverInstance = null
    }
}