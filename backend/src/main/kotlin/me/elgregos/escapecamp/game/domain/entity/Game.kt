package me.elgregos.escapecamp.game.domain.entity

import java.time.LocalDateTime
import java.util.*

data class Game(
    val id: UUID,
    val createdAt: LocalDateTime,
    val createdBy: UUID,
    val updatedAt: LocalDateTime = createdAt,
    val updatedBy: UUID = createdBy,
    val teams: List<Team> = listOf(),
    val startedAt: LocalDateTime? = null
) {
    fun addTeam(team: Team, addedAt: LocalDateTime) =
        copy(
            updatedAt = addedAt,
            updatedBy = team.id,
            teams = teams.toMutableList().also { it.add(team) }
        )

    fun isTeamNameAvailable(teamName: String) = teams.none { it.name == teamName }

    fun canAssignRiddleToTeam(teamId: UUID) =
        teams.find { it.id == teamId }?.previousRiddleSolved()?:false

    fun teamRegistrationOrder(teamId: UUID) =
        teams.indexOfFirst { it.id == teamId }

    fun assignRiddleToTeam(teamId: UUID, riddle: Riddle) =
        copy(
            updatedAt = riddle.assignedAt,
            updatedBy = teamId,
            teams = teams.map { team -> if(team.id == teamId) team.assignRiddle(riddle) else team })

    fun checkIfTeamExists(teamId: UUID) = teams.map{ team -> team.id }.contains(teamId)


}

