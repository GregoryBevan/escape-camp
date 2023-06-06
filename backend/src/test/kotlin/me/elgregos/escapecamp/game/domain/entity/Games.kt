package me.elgregos.escapecamp.game.domain.entity

import me.elgregos.escapecamp.config.security.organizerId
import me.elgregos.escapecamp.game.domain.event.lockedAndLoadedTeamAddedAt
import me.elgregos.escapecamp.game.domain.event.sherUnlockTeamAddedAt
import java.time.LocalDateTime
import java.util.*

val unknownGameId: UUID = UUID.fromString("07c905e7-8179-4b59-a65a-510a4e1de4d3")
val escapeCampId: UUID = UUID.fromString("981e1b04-ecc6-48b3-b750-58f20faa5e05")
val escapeCampCreatorId: UUID = organizerId
val escapeCampCreatedAt: LocalDateTime = LocalDateTime.of(2023, 5, 19, 21, 40, 18)

val escapeCamp = Game(escapeCampId, escapeCampCreatedAt, organizerId)

val escapeCampAfterLockedAndLoadedTeamAdded =
    escapeCamp.copy(
        updatedAt = lockedAndLoadedTeamAddedAt,
        updatedBy = lockedAndLoadedTeamId,
        teams = listOf(lockedAndLoadedTeam)
    )

val escapeCampAfterGameStarted =
    escapeCamp.copy(
        updatedAt = sherUnlockTeamAddedAt,
        updatedBy = sherUnlockTeamId,
        teams = listOf(lockedAndLoadedTeam, jeepersKeypersTeam, theEscapePeasTeam, sherUnlockTeam)
    )

val escapeCampAfterLockedAndLoadedFirstRiddleAssigned =
    escapeCamp.copy(
        updatedAt = lockedAndLoadedFirstRiddleAssignedAt,
        updatedBy = lockedAndLoadedTeamId,
        teams = listOf(lockedAndLoadedTeamAfterFirstRiddleAssigned, jeepersKeypersTeam, theEscapePeasTeam, sherUnlockTeam)
    )

val escapeCampAfterAllFirstRiddleAssigned =
    escapeCamp.copy(
        updatedAt = lockedAndLoadedFirstRiddleAssignedAt,
        updatedBy = lockedAndLoadedTeamId,
        teams = listOf( lockedAndLoadedTeamAfterFirstRiddleAssigned, jeepersKeypersTeamAfterFirstRiddleAssigned, theEscapePeasTeamAfterFirstRiddleAssigned, sherUnlockTeamAfterFirstRiddleAssigned)
    )

val escapeCampAfterLockedAndLoadedFirstRiddleSolved =
    escapeCamp.copy(
        updatedAt = lockedAndLoadedFirstRiddleSolvedAt,
        updatedBy = lockedAndLoadedTeamId,
        teams = listOf(lockedAndLoadedTeamAfterFirstRiddleSolved, jeepersKeypersTeamAfterFirstRiddleAssigned, theEscapePeasTeamAfterFirstRiddleAssigned, sherUnlockTeamAfterFirstRiddleAssigned)
    )
