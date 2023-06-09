package me.elgregos.escapecamp.config.exception

import org.springframework.http.HttpStatus
import java.util.*

sealed class GameException(
    override val message: String,
    override val cause: Throwable? = null,
    open val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
) : Exception(message, cause) {

    class GameNotFoundException(gameId: UUID) :
        GameException("Game with id $gameId has not been found", status = HttpStatus.NOT_FOUND)

    class TeamNumberLimitExceededException :
        GameException("Number of team limit reached", status = HttpStatus.BAD_REQUEST)

    class TeamNameNotAvailableException(teamName: String) :
        GameException("Team with name $teamName already exists", status = HttpStatus.BAD_REQUEST)

    class TeamNotFoundException(teamId: UUID) :
        GameException("Team with id $teamId has not been found", status = HttpStatus.NOT_FOUND)

    class GameNotStartedException :
        GameException("Game has not yet started. Wait for other teams to be added", status = HttpStatus.BAD_REQUEST)

    class PreviousRiddleNotSolvedException :
        GameException("Previous riddle not solved yet", status = HttpStatus.BAD_REQUEST)

    class UnexpectedRiddleSolutionException(riddleName: String) :
        GameException("The riddle $riddleName doesn't correspond to last unsolved riddle of the team", status = HttpStatus.BAD_REQUEST)

    class IncorrectSolutionException(riddleName: String, solution: String) :
        GameException("The submitted solution \"$solution\" for riddle $riddleName is incorrect", status = HttpStatus.BAD_REQUEST)

}