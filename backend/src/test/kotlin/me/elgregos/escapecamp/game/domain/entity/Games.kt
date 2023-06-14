package me.elgregos.escapecamp.game.domain.entity

import me.elgregos.escapecamp.config.security.organizerId
import java.time.LocalDateTime
import java.util.*

val unknownGameId: UUID = UUID.fromString("07c905e7-8179-4b59-a65a-510a4e1de4d3")
val escapeCampId: UUID = UUID.fromString("981e1b04-ecc6-48b3-b750-58f20faa5e05")
val escapeCampCreatedAt: LocalDateTime = LocalDateTime.of(2023, 5, 19, 21, 40, 18)
val lockedAndLoadedTeamAddedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 31)
val jeepersKeypersTeamAddedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 35)

val escapeCamp = Game(escapeCampId, createdAt = escapeCampCreatedAt, createdBy = organizerId, riddles = riddles)

val escapeCampAfterLockedAndLoadedTeamAdded =
    escapeCamp.copy(
        version = 2,
        updatedAt = lockedAndLoadedTeamAddedAt,
        updatedBy = lockedAndLoadedTeamId,
        teams = listOf(lockedAndLoadedTeam)
    )

val escapeCampAfterGameStarted =
    escapeCamp.copy(
        version = 3,
        updatedAt = jeepersKeypersTeamAddedAt,
        updatedBy = jeepersKeypersTeamId,
        teams = listOf(lockedAndLoadedTeam, jeepersKeypersTeam),
        startedAt = jeepersKeypersTeamAddedAt
    )

val escapeCampAfterLockedAndLoadedFirstRiddleAssigned =
    escapeCampAfterGameStarted.copy(
        version = 4,
        updatedAt = lockedAndLoadedFirstRiddleAssignedAt,
        updatedBy = lockedAndLoadedTeamId,
        teams = listOf(lockedAndLoadedTeamAfterFirstRiddleAssigned, jeepersKeypersTeam)
    )

val escapeCampAfterAllFirstRiddleAssigned =
    escapeCampAfterGameStarted.copy(
        version = 5,
        updatedAt = jeepersKeypersFirstRiddleAssignedAt,
        updatedBy = jeepersKeypersTeamId,
        teams = listOf( lockedAndLoadedTeamAfterFirstRiddleAssigned, jeepersKeypersTeamAfterFirstRiddleAssigned)
    )

val escapeCampAfterJeepersKeypersFirstRiddleSolved =
    escapeCampAfterGameStarted.copy(
        version = 6,
        updatedAt = jeepersKeypersFirstRiddleSolvedAt,
        updatedBy = jeepersKeypersTeamId,
        teams = listOf(lockedAndLoadedTeamAfterFirstRiddleAssigned, jeepersKeypersTeamAfterFirstRiddleSolved)
    )

val escapeCampAfterJeepersKeypersSecondRiddleAssigned =
    escapeCampAfterGameStarted.copy(
        version = 7,
        updatedAt = jeepersKeypersSecondRiddleAssignedAt,
        updatedBy = jeepersKeypersTeamId,
        teams = listOf(lockedAndLoadedTeamAfterFirstRiddleAssigned, jeepersKeypersTeamAfterSecondRiddleAssigned)
    )

val escapeCampAfterLockedAndLoadedFirstRiddleSolved =
    escapeCampAfterGameStarted.copy(
        version = 8,
        updatedAt = lockedAndLoadedFirstRiddleSolvedAt,
        updatedBy = lockedAndLoadedTeamId,
        teams = listOf(lockedAndLoadedTeamAfterFirstRiddleSolved, jeepersKeypersTeamAfterSecondRiddleAssigned)
    )


val escapeCampAfterLockedAndLoadedSecondRiddleAssigned =
    escapeCampAfterGameStarted.copy(
        version = 9,
        updatedAt = lockedAndLoadedSecondRiddleAssignedAt,
        updatedBy = lockedAndLoadedTeamId,
        teams = listOf(lockedAndLoadedTeamAfterSecondRiddleAssigned, jeepersKeypersTeamAfterSecondRiddleAssigned)
    )

val escapeCampAfterJeepersKeypersSecondRiddleSolved =
    escapeCampAfterGameStarted.copy(
        version = 10,
        updatedAt = jeepersKeypersSecondRiddleSolvedAt,
        updatedBy = jeepersKeypersTeamId,
        teams = listOf(lockedAndLoadedTeamAfterSecondRiddleAssigned, jeepersKeypersTeamAfterSecondRiddleSolved)
    )

val escapeCampWithWinner =
    escapeCampAfterGameStarted.copy(
        version = 11,
        updatedAt = jeepersKeypersSecondRiddleSolvedAt,
        updatedBy = jeepersKeypersTeamId,
        teams = listOf(lockedAndLoadedTeamAfterSecondRiddleAssigned, jeepersKeypersTeamAfterSecondRiddleSolved),
        winner = jeepersKeypersTeamId
    )

val escapeCampAfterLockAndLoadedSecondRiddleSolved =
    escapeCampAfterGameStarted.copy(
        version = 12,
        updatedAt = lockedAndLoadedSecondRiddleSolvedAt,
        updatedBy = lockedAndLoadedTeamId,
        teams = listOf(lockedAndLoadedTeamAfterSecondRiddleSolved, jeepersKeypersTeamAfterSecondRiddleSolved),
        winner = jeepersKeypersTeamId
    )
