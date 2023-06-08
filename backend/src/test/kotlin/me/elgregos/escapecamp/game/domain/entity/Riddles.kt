package me.elgregos.escapecamp.game.domain.entity

import java.time.LocalDateTime

val lockedAndLoadedFirstRiddleAssignedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 36, 12)
val lockedAndLoadedFirstRiddle = Riddle("riddle-1", lockedAndLoadedFirstRiddleAssignedAt)

val sherUnlockFirstRiddleAssignedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 36, 33)
val sherUnlockFirstRiddle = Riddle("riddle-4", sherUnlockFirstRiddleAssignedAt)

val theEscapePeasFirstRiddleAssignedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 36, 45)
val theEscapePeasFirstRiddle = Riddle("riddle-3", theEscapePeasFirstRiddleAssignedAt)

val jeepersKeypersFirstRiddleFirstRiddleAssignedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 37, 4)
val jeepersKeypersFirstRiddleFirstRiddle = Riddle("riddle-3", jeepersKeypersFirstRiddleFirstRiddleAssignedAt)

val lockedAndLoadedFirstRiddleSolvedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 41, 56)
val lockedAndLoadedFirstSolvedRiddle = Riddle("riddle-2", lockedAndLoadedFirstRiddleAssignedAt, lockedAndLoadedFirstRiddleSolvedAt)
