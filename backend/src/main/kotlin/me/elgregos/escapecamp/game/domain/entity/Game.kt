package me.elgregos.escapecamp.game.domain.entity

import me.elgregos.reakteves.domain.JsonConvertible
import java.time.LocalDateTime
import java.util.*

data class Game(
    val id: UUID,
    val version: Int = 1,
    val createdAt: LocalDateTime,
    val createdBy: UUID,
    val updatedAt: LocalDateTime = createdAt,
    val updatedBy: UUID = createdBy,
    val teams: List<Team> = listOf(),
    val startedAt: LocalDateTime? = null,
    val winner: UUID? = null
): JsonConvertible {
    fun addTeam(team: Team, addedAt: LocalDateTime) =
        copy(
            version = version + 1,
            updatedAt = addedAt,
            updatedBy = team.id,
            teams = teams.toMutableList().also { it.add(team) }
        )

    fun assignRiddleToTeam(teamId: UUID, assignedAt: LocalDateTime) =
        copy(
            version = version + 1,
            updatedAt = assignedAt,
            updatedBy = teamId,
            teams = teams.map { team -> if (team.id == teamId) team.assignRiddle(Riddle(riddles[nextTeamRiddleIndex(teamId)].first, assignedAt)) else team })


    fun solveLastAssignedRiddleOfTeam(teamId: UUID, solvedAt: LocalDateTime) =
        copy(
            version = version + 1,
            updatedAt = solvedAt,
            updatedBy = teamId,
            teams = teams.map { team -> if (team.id == teamId) team.solveLastUnsolvedRiddle(solvedAt) else team })

    fun isTeamNameAvailable(teamName: String) = teams.none { it.name == teamName }

    fun canAssignRiddleToTeam(teamId: UUID) =
        teams.find { it.id == teamId }?.hasPreviousRiddleSolved() ?: false

    fun teamRegistrationOrder(teamId: UUID) =
        teams.indexOfFirst { it.id == teamId }

    fun checkIfTeamExists(teamId: UUID) = teams.map { team -> team.id }.contains(teamId)

    fun teamLastUnsolvedRiddle(teamId: UUID) =
        teams.find { it.id == teamId }?.lastUnsolvedRiddle()

    fun checkIfIsFirstTeamToSolveAllRiddle() =
        winner == null && teams.any { it.hasSolvedAllRiddles() }

    private fun nextTeamRiddleIndex(teamId: UUID) =
        (teamRegistrationOrder(teamId) + numberOfSolvedRiddleByTeam(teamId)).mod(4)

    private fun numberOfSolvedRiddleByTeam(teamId: UUID) =
        teams.find { it.id == teamId }?.numberOfSolvedRiddles() ?: 0

}

