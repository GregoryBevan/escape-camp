package me.elgregos.escapecamp.game.domain.entity

import java.time.Duration
import java.util.UUID

data class LeaderBoard(val gameId: UUID, val lines: List<Line>) {

    companion object {
        fun ofGame(game: Game) =
            game.contestants
                .fold(mutableListOf<Line>()) { acc, contestant ->
                    acc.add(Line(contestant.name, contestant.numberOfSolvedRiddles(), contestant.timeToSolveRiddles()))
                    acc
                }
                .let { LeaderBoard(game.id, it) }
    }

}

data class Line(val contestantName: String, val solvedRiddlesNumber: Int, val timeToSolve: Duration)
