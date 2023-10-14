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

    class ContestantNumberLimitExceededException :
        GameException("Number of contestant limit reached", status = HttpStatus.BAD_REQUEST)

    class ContestantNameNotAvailableException(contestantName: String) :
        GameException("Contestant with name $contestantName already exists", status = HttpStatus.BAD_REQUEST)

    class ContestantNotFoundException(contestantId: UUID) :
        GameException("Contestant with id $contestantId has not been found", status = HttpStatus.NOT_FOUND)

    class GameNotStartedException :
        GameException("Game has not yet started. Wait for other contestants to enroll", status = HttpStatus.BAD_REQUEST)

    class RiddleUnlockedNotAllowedException :
        GameException("Could not get next riddle with this type of contestant enrollment", status = HttpStatus.BAD_REQUEST)

    class AllRiddlesAlreadyUnlockedException :
        GameException("All riddles are already unlocked", status = HttpStatus.BAD_REQUEST)

    class PreviousRiddleNotSolvedException :
        GameException("Previous riddle not solved yet", status = HttpStatus.BAD_REQUEST)

    class UnexpectedRiddleSolutionException(riddleName: String) :
        GameException("The riddle $riddleName doesn't correspond to last unsolved riddle of the contestant", status = HttpStatus.BAD_REQUEST)

    class IncorrectSolutionException(riddleName: String, solution: String) :
        GameException("The submitted solution \"$solution\" for riddle $riddleName is incorrect", status = HttpStatus.BAD_REQUEST)

}
