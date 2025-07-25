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
import org.example.project.server.TrackingServer
import org.example.project.shipment.observer.ShipmentObserver
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
    val observers = remember { mutableStateListOf<ShipmentObserver>() }
    var shipmentIdInput by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Start simulation on launch
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            TrackingServer.runServer()
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
                        var shipment = TrackingServer.findShipment(id)
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
                            observers.add(observer)
                        } else {
                            errorMessage = "Shipment with ID '$id' not found."
                            showErrorDialog = true
                        }
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
                if (showErrorDialog) {
                    ErrorPopup(errorMessage) {
                        showErrorDialog = false
                        errorMessage = ""
                    }
                }
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
                                        TrackingServer.findShipment(observer.shipmentId)?.removeObserver(observer)
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

@Composable
fun ErrorPopup(errorMessage: String?, onDismiss: () -> Unit) {
    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(onClick = onDismiss) { Text("OK") }
            },
            title = { Text("Error") },
            text = { Text(errorMessage) }
        )
    }
}

