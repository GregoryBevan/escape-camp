package me.elgregos.escapecamp.game.domain.entity

import me.elgregos.escapecamp.game.domain.entity.EnrollmentType.LIMITED_TO_RIDDLE_NUMBER
import me.elgregos.escapecamp.game.domain.entity.EnrollmentType.UNLIMITED
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
    val enrollmentType: EnrollmentType = LIMITED_TO_RIDDLE_NUMBER,
    val contestants: List<Contestant> = listOf(),
    val startedAt: LocalDateTime? = null,
    val currentRiddle: Int? = null,
    val winner: UUID? = null
) : DomainEntity<UUID, UUID> {

    fun enrollContestant(contestant: Contestant, enrolledAt: LocalDateTime) =
        copy(
            version = version + 1,
            updatedAt = enrolledAt,
            updatedBy = contestant.id,
            contestants = contestants.toMutableList().also { it.add(contestant) }
        )

    fun contestantNameAvailable(contestantName: String) = contestants.none { it.name == contestantName }

    fun contestantLimitNotReached() = enrollmentType == UNLIMITED || contestants.size < riddles.size

    fun ableToStartAutomatically() =
        enrollmentType == LIMITED_TO_RIDDLE_NUMBER && contestants.size == riddles.size

    fun ableToUnlockNextRiddle() =
        enrollmentType == UNLIMITED && (currentRiddle == null || currentRiddle < riddles.size - 1)

    fun nextRiddleToUnlock() = if (currentRiddle == null) 0 else currentRiddle + 1

    fun started() = startedAt != null

    fun canAssignRiddleToContestant(contestantId: UUID) =
        contestants.find { it.id == contestantId }?.let { contestant ->
            when (enrollmentType) {
                LIMITED_TO_RIDDLE_NUMBER -> contestant.hasPreviousRiddleSolved()
                UNLIMITED -> if(currentRiddle != null)  contestant.numberOfSolvedRiddles() == currentRiddle  else false
            }
        } ?: false

    fun assignRiddleToContestant(contestantId: UUID, assignedAt: LocalDateTime) =
        copy(
            version = version + 1,
            updatedAt = assignedAt,
            updatedBy = contestantId,
            contestants = contestants.map { contestant ->
                if (contestant.id == contestantId) contestant.assignRiddle(
                    Riddle(riddles[nextContestantRiddleIndex(contestantId)].first, assignedAt
                    )
                ) else contestant
            })


    fun solveLastAssignedRiddleOfContestant(contestantId: UUID, solvedAt: LocalDateTime) =
        copy(
            version = version + 1,
            updatedAt = solvedAt,
            updatedBy = contestantId,
            contestants = contestants.map { contestant ->
                if (contestant.id == contestantId) contestant.solveLastUnsolvedRiddle(
                    solvedAt
                ) else contestant
            })

    fun contestantEnrollmentOrder(contestantId: UUID) =
        contestants.indexOfFirst { it.id == contestantId }

    fun checkIfContestantExists(contestantId: UUID) =
        contestants.map { contestant -> contestant.id }.contains(contestantId)

    fun contestantLastUnsolvedRiddle(contestantId: UUID) =
        contestants.find { it.id == contestantId }?.lastUnsolvedRiddle()

    fun checkIfIsFirstContestantToSolveAllRiddle() =
        winner == null && contestants.any { it.hasSolvedAllRiddles(riddles) }

    private fun nextContestantRiddleIndex(contestantId: UUID) =
        when(enrollmentType) {
            LIMITED_TO_RIDDLE_NUMBER -> (contestantEnrollmentOrder(contestantId) + numberOfSolvedRiddleByContestant(contestantId)).mod(riddles.size)
            UNLIMITED -> currentRiddle!!
        }

    private fun numberOfSolvedRiddleByContestant(contestantId: UUID) =
        contestants.find { it.id == contestantId }?.numberOfSolvedRiddles() ?: 0

}

