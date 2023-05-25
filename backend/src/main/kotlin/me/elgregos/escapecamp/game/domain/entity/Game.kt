package me.elgregos.escapecamp.game.domain.entity

import java.time.LocalDateTime
import java.util.*

data class Game(
    val id: UUID,
    val createdAt: LocalDateTime,
    val teams: List<Team> = listOf()
) {
    fun addTeam(id: UUID, name: String) =
        copy(teams = teams.toMutableList().also { it.add(Team(id, name)) })

}

