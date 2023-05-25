package me.elgregos.escapecamp.game.domain.entity

import java.time.LocalDateTime
import java.util.*

val escapeCampId: UUID = UUID.fromString("981e1b04-ecc6-48b3-b750-58f20faa5e05")
val escapeCampCreatorId: UUID = UUID.fromString("b4ad63e8-f221-4c71-8e11-cf30e808baf0")
val escapeCampCreatedAt: LocalDateTime = LocalDateTime.of(2023, 5, 19, 21, 40, 18)

val escapeCamp = Game(escapeCampId, escapeCampCreatedAt)

val escapeCampAfterLockedAndLoadedTeamAdded = Game(escapeCampId, escapeCampCreatedAt, listOf(lockedAndLoadedTeam))