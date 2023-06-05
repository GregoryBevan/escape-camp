package me.elgregos.escapecamp.game.domain.entity

import java.time.LocalDateTime

val lockedAndLoadedFirstRiddleAssignedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 36, 12)

val lockedAndLoadedFirstRiddle = Riddle("riddle-1", lockedAndLoadedFirstRiddleAssignedAt)

val sherUnlockFirstRiddleAssignedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 36, 33)

val sherUnlockFirstRiddle = Riddle("riddle-4", sherUnlockFirstRiddleAssignedAt)
