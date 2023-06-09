package me.elgregos.escapecamp.game.domain.entity

import java.time.LocalDateTime

val riddles: List<Pair<String, String>> = listOf(
    Pair("riddle-1", "solution-1"),
    Pair("riddle-2", "solution-2"),
    Pair("riddle-3", "solution-3"),
    Pair("riddle-4", "solution-4")
)

data class Riddle(
    val name: String,
    val assignedAt: LocalDateTime,
    val solvedAt: LocalDateTime? = null
) {
    fun hasBeenSolved() = solvedAt != null

    fun solved(solvedAt: LocalDateTime) = copy(solvedAt = solvedAt)

}