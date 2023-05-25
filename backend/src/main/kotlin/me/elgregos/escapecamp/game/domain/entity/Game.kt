package me.elgregos.escapecamp.game.domain.entity

import java.time.LocalDateTime
import java.util.*

data class Game(
    val id: UUID,
    val createdAt: LocalDateTime,
    val teams: List<Team> = listOf()
) {
    fun addTeam(team: Team) =
        copy(teams = teams.toMutableList().also { it.add(team) })

}

