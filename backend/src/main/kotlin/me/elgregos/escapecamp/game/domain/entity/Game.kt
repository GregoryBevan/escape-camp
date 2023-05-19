package me.elgregos.escapecamp.game.domain.entity

import java.util.*

data class Game(private val teams: MutableList<Team> = mutableListOf()) {
    fun addTeam(id: UUID, name: String) =
        if (teams.size < 4) teams.add(Team(id, name)) else throw TeamLimitExceeded()

    fun teams() = teams.toList()

}

