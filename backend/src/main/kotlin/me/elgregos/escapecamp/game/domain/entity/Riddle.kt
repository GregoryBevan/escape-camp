package me.elgregos.escapecamp.game.domain.entity

import java.time.LocalDateTime

val riddles: List<Pair<String, String>> = listOf(
    Pair("riddle-1", "event sourcing"),
    Pair("riddle-2", "reactive"),
    Pair("riddle-3", "craft"),
    Pair("riddle-4", "DDD")
)

data class Riddle(
    val name: String,
    val assignedAt: LocalDateTime,
    val solvedAt: LocalDateTime? = null
) {
    fun hasBeenSolved() = solvedAt != null

    fun solved(solvedAt: LocalDateTime) = copy(solvedAt = solvedAt)

}
