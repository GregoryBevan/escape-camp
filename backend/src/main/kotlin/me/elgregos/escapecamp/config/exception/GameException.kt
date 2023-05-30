package me.elgregos.escapecamp.config.exception

import org.springframework.http.HttpStatus

sealed class GameException(
    override val message: String,
    override val cause: Throwable? = null,
    open val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
) : Exception(message, cause) {

    data class TeamNumberLimitExceededException(override val message: String = "Number of team is limited to 4") :
        GameException(message, status = HttpStatus.BAD_REQUEST)

    data class TeamNameNotAvailableException(
        val teamName: String,
        override val message: String = "Team with name $teamName already exists"
    ) :
        GameException(message, status = HttpStatus.BAD_REQUEST)

}