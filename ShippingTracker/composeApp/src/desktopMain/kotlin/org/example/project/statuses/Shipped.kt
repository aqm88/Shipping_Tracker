package org.example.project.statuses

object Shipped: Status {
    override fun get(): String {
        return "Shipped"
    }
}