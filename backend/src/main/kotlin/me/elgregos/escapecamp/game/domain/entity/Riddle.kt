package me.elgregos.escapecamp.game.domain.entity

import java.time.LocalDateTime

data class Riddle(
    val name: String,
    val assignedAt: LocalDateTime,
    val solvedAt: LocalDateTime? = null
) {
    fun hasBeenSolved() = solvedAt != null

    fun solved(solvedAt: LocalDateTime) = copy(solvedAt = solvedAt)

}
