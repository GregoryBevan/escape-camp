package me.elgregos.escapecamp.game.domain.entity

import me.elgregos.escapecamp.config.security.organizerId
import java.time.LocalDateTime
import java.util.*

val unknownGameId: UUID = UUID.fromString("07c905e7-8179-4b59-a65a-510a4e1de4d3")
val escapeCampId: UUID = UUID.fromString("981e1b04-ecc6-48b3-b750-58f20faa5e05")
val escapeCampCreatorId: UUID = organizerId
val escapeCampCreatedAt: LocalDateTime = LocalDateTime.of(2023, 5, 19, 21, 40, 18)

val escapeCamp = Game(escapeCampId, escapeCampCreatedAt, organizerId)

val escapeCampAfterLockedAndLoadedTeamAdded =
    Game(escapeCampId, escapeCampCreatedAt, organizerId, teams = listOf(lockedAndLoadedTeam))