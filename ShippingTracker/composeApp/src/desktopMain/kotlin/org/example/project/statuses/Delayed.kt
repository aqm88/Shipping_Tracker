package org.example.project.statuses

object Delayed: Status {
    override fun get(): String {
        return "Delayed"
    }
}