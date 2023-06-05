package me.elgregos.escapecamp.game.domain.entity

import java.time.LocalDateTime

val riddleNames: List<String> = listOf("riddle-1", "riddle-2", "riddle-3", "riddle-4")

data class Riddle(
    val name: String,
    val assignedAt: LocalDateTime,
    val solvedAt: LocalDateTime? = null
)