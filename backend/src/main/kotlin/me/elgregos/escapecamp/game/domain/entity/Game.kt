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
        teams.find { it.id == teamId }?.hasPreviousRiddleSolved() ?: false

    fun teamRegistrationOrder(teamId: UUID) =
        teams.indexOfFirst { it.id == teamId }

    fun assignRiddleToTeam(teamId: UUID, assignedAt: LocalDateTime) =
        copy(
            updatedAt = assignedAt,
            updatedBy = teamId,
            teams = teams.map { team -> if (team.id == teamId) team.assignRiddle(Riddle(riddles[nextTeamRiddleIndex(teamId)].first, assignedAt)) else team })

    fun checkIfTeamExists(teamId: UUID) = teams.map { team -> team.id }.contains(teamId)

    fun teamLastUnsolvedRiddle(teamId: UUID) =
        teams.find { it.id == teamId }?.lastUnsolvedRiddle()


    fun solveLastAssignedRiddleOfTeam(teamId: UUID, solvedAt: LocalDateTime) =
        copy(
            updatedAt = solvedAt,
            updatedBy = teamId,
            teams = teams.map { team -> if (team.id == teamId) team.solveLastUnsolvedRiddle(solvedAt) else team })

    private fun nextTeamRiddleIndex(teamId: UUID) =
        teamRegistrationOrder(teamId) + numberOfSolvedRiddleByTeam(teamId)

    private fun numberOfSolvedRiddleByTeam(teamId: UUID) =
        teams.find { it.id == teamId }?.numberOfSolvedRiddles() ?: 0

}

