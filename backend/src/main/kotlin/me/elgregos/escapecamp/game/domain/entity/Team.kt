package me.elgregos.escapecamp.game.domain.entity

import me.elgregos.reakteves.domain.JsonConvertible
import java.time.LocalDateTime
import java.util.*

data class Team(
    val id: UUID,
    val name: String,
    val riddles: List<Riddle> = listOf()
): JsonConvertible {

    fun assignRiddle(riddle: Riddle) =
        copy(
             riddles = riddles.toMutableList().apply { add(riddle) }.toList()
         )

    fun hasPreviousRiddleSolved() =
        riddles.isEmpty() || riddles.last().hasBeenSolved()

    fun lastUnsolvedRiddle() =
        riddles.last { riddle -> !riddle.hasBeenSolved() }

    fun solveLastUnsolvedRiddle(solvedAt: LocalDateTime) =
        copy(
            riddles = riddles.toMutableList()
                .apply { set(riddles.size-1, lastUnsolvedRiddle().solved(solvedAt)) }
                .toList()
        )

    fun numberOfSolvedRiddles() =
        riddles.filter { riddle -> riddle.solvedAt != null }.size

    fun hasSolvedAllRiddles() = numberOfSolvedRiddles() == 4


}