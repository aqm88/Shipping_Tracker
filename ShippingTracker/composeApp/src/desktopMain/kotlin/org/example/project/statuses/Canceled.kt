package org.example.project.statuses

object Canceled: Status {
    override fun get(): String {
        return "Canceled"
    }
}