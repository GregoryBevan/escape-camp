package me.elgregos.escapecamp.game.domain.entity

import me.elgregos.reakteves.domain.entity.DomainEntity
import java.time.LocalDateTime
import java.util.*

data class Game(
    override val id: UUID,
    override val version: Int = 1,
    override val createdAt: LocalDateTime,
    override val createdBy: UUID,
    override val updatedAt: LocalDateTime = createdAt,
    override val updatedBy: UUID = createdBy,
    val riddles: List<Pair<String, String>>,
    val teams: List<Team> = listOf(),
    val startedAt: LocalDateTime? = null,
    val winner: UUID? = null
) : DomainEntity<UUID, UUID> {
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
            teams = teams.map { team ->
                if (team.id == teamId) team.assignRiddle(
                    Riddle(
                        riddles[nextTeamRiddleIndex(
                            teamId
                        )].first, assignedAt
                    )
                ) else team
            })


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
        winner == null && teams.any { it.hasSolvedAllRiddles(riddles) }

    private fun nextTeamRiddleIndex(teamId: UUID) =
        (teamRegistrationOrder(teamId) + numberOfSolvedRiddleByTeam(teamId)).mod(riddles.size)

    private fun numberOfSolvedRiddleByTeam(teamId: UUID) =
        teams.find { it.id == teamId }?.numberOfSolvedRiddles() ?: 0

}

