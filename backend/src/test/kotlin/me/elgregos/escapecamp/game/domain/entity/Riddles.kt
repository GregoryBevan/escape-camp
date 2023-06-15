package me.elgregos.escapecamp.game.domain.entity

import java.time.LocalDateTime

val riddles: List<Pair<String, String>> = listOf(
    Pair("riddle-1", "solution-1"),
    Pair("riddle-2", "solution-2")
)

val lockedAndLoadedFirstRiddleAssignedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 36, 12)
val lockedAndLoadedFirstRiddle = Riddle("riddle-1", lockedAndLoadedFirstRiddleAssignedAt)

val jeepersKeypersFirstRiddleAssignedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 37, 4)
val jeepersKeypersFirstRiddle = Riddle("riddle-2", jeepersKeypersFirstRiddleAssignedAt)

val jeepersKeypersFirstRiddleSolvedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 41, 45)
val jeepersKeypersFirstSolvedRiddle = Riddle("riddle-2", jeepersKeypersFirstRiddleAssignedAt, jeepersKeypersFirstRiddleSolvedAt)

val jeepersKeypersSecondRiddleAssignedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 42, 28)
val jeepersKeypersSecondRiddle = Riddle("riddle-1", jeepersKeypersSecondRiddleAssignedAt)

val lockedAndLoadedFirstRiddleSolvedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 43, 56)
val lockedAndLoadedFirstSolvedRiddle = Riddle("riddle-1", lockedAndLoadedFirstRiddleAssignedAt, lockedAndLoadedFirstRiddleSolvedAt)

val lockedAndLoadedSecondRiddleAssignedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 44, 2)
val lockedAndLoadedSecondRiddle = Riddle("riddle-2", lockedAndLoadedSecondRiddleAssignedAt)

val jeepersKeypersSecondRiddleSolvedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 47, 36)
val jeepersKeypersSecondSolvedRiddle = Riddle("riddle-1", jeepersKeypersSecondRiddleAssignedAt, jeepersKeypersSecondRiddleSolvedAt)

val lockedAndLoadedSecondRiddleSolvedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 50, 5)
val lockedAndLoadedSecondSolvedRiddle = Riddle("riddle-2", lockedAndLoadedSecondRiddleAssignedAt, lockedAndLoadedSecondRiddleSolvedAt)