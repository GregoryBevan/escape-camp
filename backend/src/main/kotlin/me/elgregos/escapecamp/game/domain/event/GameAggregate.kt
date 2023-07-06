package me.elgregos.escapecamp.game.domain.event

import me.elgregos.escapecamp.config.exception.GameException.*
import me.elgregos.escapecamp.game.domain.entity.Game
import me.elgregos.escapecamp.game.domain.entity.Team
import me.elgregos.escapecamp.game.domain.event.GameEvent.*
import me.elgregos.escapecamp.game.domain.service.RiddleSolutionChecker
import me.elgregos.reakteves.domain.JsonConvertible
import me.elgregos.reakteves.domain.event.EventStore
import me.elgregos.reakteves.domain.event.JsonAggregate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.*

class GameAggregate(
    private val gameId: UUID,
    private val userId: UUID,
    private val riddleSolutionChecker: RiddleSolutionChecker,
    eventStore: EventStore<GameEvent, UUID, UUID>
) :
    JsonAggregate<GameEvent, UUID, UUID>(gameId, eventStore) {

    fun createGame(riddles: List<Pair<String, String>>, startedAt: LocalDateTime): Flux<GameEvent> =
        Flux.just(GameCreated(gameId, userId, startedAt, riddles))

    fun addTeam(team: Team, addedAt: LocalDateTime): Flux<GameEvent> =
        previousState()
            .filter { !it.isEmpty }
            .switchIfEmpty(Mono.error { GameNotFoundException(gameId) })
            .map { JsonConvertible.fromJson(it, Game::class.java) }
            .filter { game -> game.isTeamNameAvailable(team.name) }
            .switchIfEmpty(Mono.error { TeamNameNotAvailableException(team.name) })
            .filter { game -> game.teams.size < game.riddles.size }
            .switchIfEmpty(Mono.error { TeamNumberLimitExceededException() })
            .map { game -> game.addTeam(team, addedAt) }
            .flatMapMany { game ->
                nextVersion()
                    .map { version -> TeamAdded(gameId, version, userId, addedAt, game.teams) }
                    .flatMap { this.applyNewEvent(it) }
                    .cast(GameEvent::class.java)
                    .concatWith(nextVersion()
                        .filter { game.teams.size == game.riddles.size }
                        .map { version -> GameStarted(gameId, version, userId, addedAt) })
            }

    fun assignTeamNextRiddle(assignedAt: LocalDateTime): Flux<GameEvent> =
        previousState()
            .filter { !it.isEmpty }
            .switchIfEmpty(Mono.error { GameNotFoundException(gameId) })
            .map { JsonConvertible.fromJson(it, Game::class.java) }
            .filter { game -> game.checkIfTeamExists(userId) }
            .switchIfEmpty(Mono.error { TeamNotFoundException(userId) })
            .filter { game -> game.teams.size == game.riddles.size }
            .switchIfEmpty(Mono.error { GameNotStartedException() })
            .filter { game -> game.canAssignRiddleToTeam(userId) }
            .switchIfEmpty(Mono.error { PreviousRiddleNotSolvedException() })
            .map { game -> game.assignRiddleToTeam(userId, assignedAt) }
            .flatMapMany { game ->
                nextVersion()
                    .map { nextVersion -> NextTeamRiddleAssigned(gameId, nextVersion, assignedAt, userId, game.teams) }
            }

    fun checkRiddleSolution(
        riddleName: String,
        submittedSolution: String,
        submittedAt: LocalDateTime
    ): Flux<GameEvent> =
        previousState()
            .filter { !it.isEmpty }
            .switchIfEmpty(Mono.error { GameNotFoundException(gameId) })
            .map { JsonConvertible.fromJson(it, Game::class.java) }
            .filter { riddleSolutionChecker.isCorrect(riddleName, submittedSolution) }
            .switchIfEmpty(Mono.error { IncorrectSolutionException(riddleName, submittedSolution) })
            .filter { game -> game.teamLastUnsolvedRiddle(userId)?.name == riddleName }
            .switchIfEmpty(Mono.error { UnexpectedRiddleSolutionException(riddleName) })
            .map { game -> game.solveLastAssignedRiddleOfTeam(userId, submittedAt) }
            .flatMapMany { game ->
                nextVersion()
                    .map { nextVersion -> RiddleSolved(gameId, nextVersion, submittedAt, userId, game.teams) }
                    .flatMap { this.applyNewEvent(it) }
                    .cast(GameEvent::class.java)
                    .concatWith(nextVersion()
                        .filter { game.checkIfIsFirstTeamToSolveAllRiddle() }
                        .map { version -> WinnerAnnounced(gameId, version, submittedAt, userId) })
            }
}
