package me.elgregos.escapecamp.game.domain.entity

import me.elgregos.escapecamp.config.security.organizerId
import java.time.LocalDateTime
import java.util.*

val unknownGameId: UUID = UUID.fromString("07c905e7-8179-4b59-a65a-510a4e1de4d3")
val escapeCampId: UUID = UUID.fromString("981e1b04-ecc6-48b3-b750-58f20faa5e05")
val escapeCampCreatedAt: LocalDateTime = LocalDateTime.of(2023, 5, 19, 21, 40, 18)
val lockedAndLoadedTeamAddedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 31)
val sherUnlockTeamAddedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 35)

val escapeCamp = Game(escapeCampId, createdAt = escapeCampCreatedAt, createdBy = organizerId)

val escapeCampAfterLockedAndLoadedTeamAdded =
    escapeCamp.copy(
        version = 2,
        updatedAt = lockedAndLoadedTeamAddedAt,
        updatedBy = lockedAndLoadedTeamId,
        teams = listOf(lockedAndLoadedTeam)
    )

val escapeCampAfterGameStarted =
    escapeCamp.copy(
        version = 6,
        updatedAt = sherUnlockTeamAddedAt,
        updatedBy = sherUnlockTeamId,
        teams = listOf(lockedAndLoadedTeam, jeepersKeypersTeam, theEscapePeasTeam, sherUnlockTeam)
    )

val escapeCampAfterLockedAndLoadedFirstRiddleAssigned =
    escapeCamp.copy(
        version = 7,
        updatedAt = lockedAndLoadedFirstRiddleAssignedAt,
        updatedBy = lockedAndLoadedTeamId,
        teams = listOf(lockedAndLoadedTeamAfterFirstRiddleAssigned, jeepersKeypersTeam, theEscapePeasTeam, sherUnlockTeam)
    )

val escapeCampAfterSherUnlockFirstRiddleAssigned =
    escapeCamp.copy(
        version = 8,
        updatedAt = sherUnlockFirstRiddleAssignedAt,
        updatedBy = sherUnlockTeamId,
        teams = listOf(lockedAndLoadedTeamAfterFirstRiddleAssigned, jeepersKeypersTeam, theEscapePeasTeam, sherUnlockTeamAfterFirstRiddleAssigned)
    )

val escapeCampAfterTheEscapePeasFirstRiddleAssigned =
    escapeCamp.copy(
        version = 9,
        updatedAt = theEscapePeasFirstRiddleAssignedAt,
        updatedBy = theEscapePeasTeamId,
        teams = listOf(lockedAndLoadedTeamAfterFirstRiddleAssigned, jeepersKeypersTeam, theEscapePeasTeamAfterFirstRiddleAssigned, sherUnlockTeamAfterFirstRiddleAssigned)
    )

val escapeCampAfterAllFirstRiddleAssigned =
    escapeCamp.copy(
        version = 10,
        updatedAt = jeepersKeypersFirstRiddleAssignedAt,
        updatedBy = jeepersKeypersTeamId,
        teams = listOf( lockedAndLoadedTeamAfterFirstRiddleAssigned, jeepersKeypersTeamAfterFirstRiddleAssigned, theEscapePeasTeamAfterFirstRiddleAssigned, sherUnlockTeamAfterFirstRiddleAssigned)
    )

val escapeCampAfterJeepersKeypersFirstRiddleSolved =
    escapeCamp.copy(
        version = 11,
        updatedAt = jeepersKeypersFirstRiddleSolvedAt,
        updatedBy = jeepersKeypersTeamId,
        teams = listOf(lockedAndLoadedTeamAfterFirstRiddleAssigned, jeepersKeypersTeamAfterFirstRiddleSolved, theEscapePeasTeamAfterFirstRiddleAssigned, sherUnlockTeamAfterFirstRiddleAssigned)
    )

val escapeCampAfterJeepersKeypersSecondRiddleAssigned =
    escapeCamp.copy(
        version = 12,
        updatedAt = jeepersKeypersSecondRiddleAssignedAt,
        updatedBy = jeepersKeypersTeamId,
        teams = listOf(lockedAndLoadedTeamAfterFirstRiddleAssigned, jeepersKeypersTeamAfterSecondRiddleAssigned, theEscapePeasTeamAfterFirstRiddleAssigned, sherUnlockTeamAfterFirstRiddleAssigned)
    )

val escapeCampAfterSherUnlockFirstRiddleSolved =
    escapeCamp.copy(
        version = 13,
        updatedAt = sherUnlockFirstRiddleSolvedAt,
        updatedBy = sherUnlockTeamId,
        teams = listOf(lockedAndLoadedTeamAfterFirstRiddleAssigned, jeepersKeypersTeamAfterSecondRiddleAssigned, theEscapePeasTeamAfterFirstRiddleAssigned, sherUnlockTeamAfterFirstRiddleSolved)
    )

val escapeCampAfterSherUnlockSecondRiddleAssigned =
    escapeCamp.copy(
        version = 14,
        updatedAt = sherUnlockSecondRiddleAssignedAt,
        updatedBy = sherUnlockTeamId,
        teams = listOf(lockedAndLoadedTeamAfterFirstRiddleAssigned, jeepersKeypersTeamAfterSecondRiddleAssigned, theEscapePeasTeamAfterFirstRiddleAssigned, sherUnlockTeamAfterSecondRiddleAssigned)
    )

val escapeCampAfterLockedAndLoadedFirstRiddleSolved =
    escapeCamp.copy(
        version = 15,
        updatedAt = lockedAndLoadedFirstRiddleSolvedAt,
        updatedBy = lockedAndLoadedTeamId,
        teams = listOf(lockedAndLoadedTeamAfterFirstRiddleSolved, jeepersKeypersTeamAfterFirstRiddleSolved, theEscapePeasTeamAfterFirstRiddleAssigned, sherUnlockTeamAfterFirstRiddleAssigned)
    )


val escapeCampAfterTheEscapePeasFourthRiddleAssigned =
    escapeCamp.copy(
        version = 34,
        updatedAt = theEscapePeasFourthRiddleAssignedAt,
        updatedBy = theEscapePeasTeamId,
        teams = listOf(lockedAndLoadedTeamAfterFourthRiddleAssigned, jeepersKeypersTeamAfterFourthRiddleAssigned, theEscapePeasTeamAfterFourthRiddleAssigned, sherUnlockTeamAfterFourthRiddleAssigned)
    )

val escapeCampAfterJeepersKeypersFourthRiddleSolved =
    escapeCamp.copy(
        version = 35,
        updatedAt = jeepersKeypersFourthRiddleSolvedAt,
        updatedBy = jeepersKeypersTeamId,
        teams = listOf(lockedAndLoadedTeamAfterFourthRiddleAssigned, jeepersKeypersTeamAfterFourthRiddleSolved, theEscapePeasTeamAfterFourthRiddleAssigned, sherUnlockTeamAfterFourthRiddleAssigned)
    )

val escapeCampWithWinner =
    escapeCamp.copy(
        version = 36,
        updatedAt = jeepersKeypersFourthRiddleSolvedAt,
        updatedBy = jeepersKeypersTeamId,
        teams = listOf(lockedAndLoadedTeamAfterFourthRiddleAssigned, jeepersKeypersTeamAfterFourthRiddleSolved, theEscapePeasTeamAfterFourthRiddleAssigned, sherUnlockTeamAfterFourthRiddleAssigned),
        winner = jeepersKeypersTeamId
    )

val escapeCampAfterSherUnlockFourthRiddleSolved =
    escapeCamp.copy(
        version = 37,
        updatedAt = sherUnlockFourthRiddleSolvedAt,
        updatedBy = sherUnlockTeamId,
        teams = listOf(lockedAndLoadedTeamAfterFourthRiddleAssigned, jeepersKeypersTeamAfterFourthRiddleSolved, theEscapePeasTeamAfterFourthRiddleAssigned, sherUnlockTeamAfterFourthRiddleSolved),
        winner = jeepersKeypersTeamId
    )
