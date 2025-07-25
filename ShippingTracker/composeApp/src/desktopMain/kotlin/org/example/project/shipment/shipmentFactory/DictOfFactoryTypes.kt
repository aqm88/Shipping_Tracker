package org.example.project.shipment.shipmentFactory


val factoryType = mapOf(
    "standard" to StandardShipmentFactory,
    "bulk" to BulkShipmentFactory,
    "express" to ExpressShipmentFactory,
    "overnight" to OvernightShipmentFactory
)