package org.example.project

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import org.example.project.shipment.ShipmentObserver
import org.example.project.simulator.TrackingSimulator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimestamp(timestamp: Long?): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    if(timestamp == null) {
        return "N/A"
    }
    return sdf.format(Date(timestamp))
}
@Composable
@Preview
fun App() {
    val simulator = remember { TrackingSimulator() }
    val observers = remember { mutableStateListOf<ShipmentObserver>() }
    var shipmentIdInput by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    // Start simulation on launch
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            simulator.runSimulation("test.txt")
        }
    }

    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                BasicTextField(
                    value = shipmentIdInput,
                    onValueChange = { shipmentIdInput = it },
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(Modifier.padding(8.dp)) {
                            if (shipmentIdInput.isEmpty()) Text("Enter Shipment ID")
                            innerTextField()
                        }
                    }
                )
                Button(onClick = {
                    val id = shipmentIdInput.trim()
                    print(id)
                    if (id.isNotEmpty()) {
                        var shipment = simulator.findShipment(id)
                        var observer: ShipmentObserver
                        if (shipment != null) {
                            observer =ShipmentObserver(
                                shipmentId = id,
                                shipmentNotes = shipment.notes,
                                shipmentUpdateHistory = shipment.updateHistory.map { it.toString() },
                                expectedShipmentDeliveryDate = shipment.expectedDeliveryDateTimestamp,
                                shipmentStatus = shipment.status.get(),
                                shipmentLocation = shipment.currentLocation
                            )
                            shipment.registerObserver(observer)
                        } else {
                            observer = ShipmentObserver(
                                shipmentId = id,
                                shipmentNotes = arrayListOf("Shipment not found in system!"),
                                shipmentUpdateHistory = emptyList(),
                                expectedShipmentDeliveryDate = null,
                                shipmentStatus = "",
                                shipmentLocation = null
                            )
                        }
                        observers.add(observer)
                        shipmentIdInput = ""
                    }
                }) {
                    Text("Follow")
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Tracked Shipments:", style = MaterialTheme.typography.titleMedium)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                observers.forEach { observer ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(Modifier.padding(8.dp)) {
                            Text("ID: ${observer.shipmentId}", style = MaterialTheme.typography.bodyLarge)
                            Text("Status: ${observer.shipmentStatus}")
                            Text("Location: ${observer.shipmentLocation ?: "Unknown"}")
                            Text("Expected Delivery: ${formatTimestamp(observer.expectedShipmentDeliveryDate)}")
                            Column {
                                Spacer(Modifier.height(8.dp))
                                Text("Notes:")
                                observer.shipmentNotes.forEach { note ->
                                    Text(note)
                                }
                                Spacer(Modifier.height(8.dp))
                                Text("Updates:")
                                observer.shipmentUpdateHistory.forEach { update ->
                                    Text(update)
                                }
                            }
                            Row {
                                Button(
                                    onClick = {
                                        // Remove observer from shipment if exists
                                        simulator.findShipment(observer.shipmentId)?.removeObserver(observer)
                                        observers.remove(observer)
                                    },
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Text("Unfollow")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}