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
    val contestants: List<Contestant> = listOf(),
    val startedAt: LocalDateTime? = null,
    val winner: UUID? = null
) : DomainEntity<UUID, UUID> {
    fun addContestant(contestant: Contestant, addedAt: LocalDateTime) =
        copy(
            version = version + 1,
            updatedAt = addedAt,
            updatedBy = contestant.id,
            contestants = contestants.toMutableList().also { it.add(contestant) }
        )

    fun assignRiddleToContestant(contestantId: UUID, assignedAt: LocalDateTime) =
        copy(
            version = version + 1,
            updatedAt = assignedAt,
            updatedBy = contestantId,
            contestants = contestants.map { contestant ->
                if (contestant.id == contestantId) contestant.assignRiddle(
                    Riddle(
                        riddles[nextContestantRiddleIndex(
                            contestantId
                        )].first, assignedAt
                    )
                ) else contestant
            })


    fun solveLastAssignedRiddleOfContestant(contestantId: UUID, solvedAt: LocalDateTime) =
        copy(
            version = version + 1,
            updatedAt = solvedAt,
            updatedBy = contestantId,
            contestants = contestants.map { contestant -> if (contestant.id == contestantId) contestant.solveLastUnsolvedRiddle(solvedAt) else contestant })

    fun isContestantNameAvailable(contestantName: String) = contestants.none { it.name == contestantName }

    fun canAssignRiddleToContestant(contestantId: UUID) =
        contestants.find { it.id == contestantId }?.hasPreviousRiddleSolved() ?: false

    fun contestantRegistrationOrder(contestantId: UUID) =
        contestants.indexOfFirst { it.id == contestantId }

    fun checkIfContestantExists(contestantId: UUID) = contestants.map { contestant -> contestant.id }.contains(contestantId)

    fun contestantLastUnsolvedRiddle(contestantId: UUID) =
        contestants.find { it.id == contestantId }?.lastUnsolvedRiddle()

    fun checkIfIsFirstContestantToSolveAllRiddle() =
        winner == null && contestants.any { it.hasSolvedAllRiddles(riddles) }

    private fun nextContestantRiddleIndex(contestantId: UUID) =
        (contestantRegistrationOrder(contestantId) + numberOfSolvedRiddleByContestant(contestantId)).mod(riddles.size)

    private fun numberOfSolvedRiddleByContestant(contestantId: UUID) =
        contestants.find { it.id == contestantId }?.numberOfSolvedRiddles() ?: 0

}

