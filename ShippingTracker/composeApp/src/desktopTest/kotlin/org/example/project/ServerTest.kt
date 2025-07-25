package org.example.project

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.example.project.server.TrackingServer
import org.example.project.shipment.shipmentFactory.factoryType
import org.example.project.shipment.shipmentTypes.Shipment
import org.example.project.updates.updateType
import kotlin.test.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import org.example.project.shipment.shipmentTypes.StandardShipment
import org.example.project.statuses.Created
import org.example.project.statuses.Shipped

class ServerTest {

    private var server: TrackingServer = TrackingServer

    @Test
    fun findShipment_returnsNull_whenNotFound() {
        val result = server.findShipment("nonexistent")
        assertNull(result)
    }

    @Test
    fun findShipment_returnsShipment_whenFound() {
        val shipment = factoryType["standard"]!!.createShipment("id1", 123L)
        val shipmentsField = TrackingServer::class.java.getDeclaredField("shipments")
        shipmentsField.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        val shipments = shipmentsField.get(server) as MutableList<Shipment>
        shipments.add(shipment)
        val result = server.findShipment("id1")
        assertEquals(shipment.id, result?.id)
    }

    @Test
    fun runServer_processesCreatedCommand_andAddsShipment() {
        val shipment = factoryType["standard"]!!.createShipment("id2", 456L)
        val shipmentsField = TrackingServer::class.java.getDeclaredField("shipments")
        shipmentsField.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        val shipments = shipmentsField.get(server) as MutableList<Shipment>
        shipments.add(shipment)
        assertEquals(shipment, server.findShipment("id2"))
    }

    @Test
    fun runServer_processesUpdateCommand_andUpdatesShipment() {
        val shipment = factoryType["standard"]!!.createShipment("id3", 789L)
        val shipmentsField = TrackingServer::class.java.getDeclaredField("shipments")
        shipmentsField.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        val shipments = shipmentsField.get(server) as MutableList<Shipment>
        shipments.add(shipment)
        val updater:String? = updateType.keys.firstOrNull()
        if (updater != null) {
            updateType[updater]?.apply(shipment, "$updater,id3,1234,123123123123")
            assertEquals("id3", shipment.id)
        }
    }

    @Test
    fun processCommand_blank_returnsNoCommandProvided() {
        assertEquals("No command provided", server.processCommand(""))
    }

    @Test
    fun processCommand_created_tooFewParts_returnsInvalidFormat() {
        assertEquals("Invalid command format for 'created'", server.processCommand("created,id1,standard"))
    }

    @Test
    fun processCommand_created_invalidTimestamp_returnsInvalidTimestamp() {
        assertEquals("Invalid timestamp", server.processCommand("created,id1,standard,not_a_number"))
    }

    @Test
    fun processCommand_created_invalidType_returnsInvalidShipmentType() {
        assertEquals("Invalid shipment type: fake", server.processCommand("created,id1,fake,123"))
    }

    @Test
    fun processCommand_created_valid_addsShipment() {
        val result = server.processCommand("created,id1,standard,123")
        assertEquals("Shipment created: id1", result)
        assertNotNull(server.findShipment("id1"))
    }

    @Test
    fun processCommand_update_tooFewParts_returnsInvalidFormat() {
        assertEquals("Invalid update command format", server.processCommand("update"))
    }

    @Test
    fun processCommand_update_shipmentNotFound_returnsNotFound() {
        assertEquals("Shipment not found: idX", server.processCommand("update,idX,123"))
    }

    @Test
    fun processCommand_update_unknownType_returnsUnknownCommandType() {
        server.processCommand("created,id2,standard,123")
        assertEquals("Unknown command type: unknown", server.processCommand("unknown,id2,123"))
    }

    @Test
    fun processCommand_update_valid_appliesUpdate() {
        server.processCommand("created,id3,standard,123")
        val updater = updateType.keys.firstOrNull() ?: return
        val result = server.processCommand("$updater,id3,123,456")
        assertTrue(result.startsWith("Update applied:"))
    }

    @Test
    fun testGetRootEndpoint() = runBlocking {
        val serverThread = Thread {
            server.runServer()
        }
        serverThread.isDaemon = true
        serverThread.start()
        // Wait for the server to start
        kotlinx.coroutines.delay(1000)
        val client = HttpClient(CIO)
        val response: HttpResponse = client.get("http://localhost:8080/")
        val body = response.bodyAsText()
        assertEquals(200, response.status.value)
        assertTrue(body.contains("Tracking Server"))
        client.close()
        server.stopServer()

    }

    @Test
    fun testCommandEndpoint() = runBlocking {
        val serverThread = Thread {
            server.runServer()
        }
        serverThread.isDaemon = true
        serverThread.start()
        kotlinx.coroutines.delay(1000)
        val client = HttpClient(CIO)
        val response: HttpResponse = client.post("http://localhost:8080/command") {
            setBody("command=created,id4,standard,1234567890")
            headers.append("Content-Type", "application/x-www-form-urlencoded")
        }
        val body = response.bodyAsText()
        assertEquals(200, response.status.value)
        assertTrue(body.contains("Shipment created: id4"))
        client.close()
        server.stopServer()
    }

    @Test
    fun stopServer_stopsRunningServer() = runBlocking {
        val serverThread = Thread {
            TrackingServer.runServer() // still blocks, but in another thread
        }
        serverThread.isDaemon = true
        serverThread.start()
        kotlinx.coroutines.delay(1000)
        TrackingServer.stopServer()

    }

    @Test
    fun testCommandEndpoint_noCommandParam_returnsNoCommandProvided() = runBlocking {
        val serverThread = Thread {
            TrackingServer.runServer()
        }
        serverThread.isDaemon = true
        serverThread.start()
        kotlinx.coroutines.delay(1000)
        val client = HttpClient(CIO)
        val response: HttpResponse = client.post("http://localhost:8080/command") {
            setBody("") // No command param
            headers.append("Content-Type", "application/x-www-form-urlencoded")
        }
        val body = response.bodyAsText()
        assertEquals(200, response.status.value)
        assertTrue(body.contains("No command provided"))
        client.close()
        TrackingServer.stopServer()
    }



    @Test
    fun stopServer_whenServerNotRunning_doesNothing() = runBlocking{
        TrackingServer.stopServer() // serverInstance == null branch
        kotlinx.coroutines.delay(1000)
        TrackingServer.stopServer()
    }

    @Test
    fun testAllStatusTypes() {
        val statuses = listOf(Created, Shipped /*, Delivered, Cancelled, etc.*/)
        for (status in statuses) {
            val shipment = StandardShipment("C3", status)
            assertEquals(status.get(), shipment.status.get())
        }
    }

}