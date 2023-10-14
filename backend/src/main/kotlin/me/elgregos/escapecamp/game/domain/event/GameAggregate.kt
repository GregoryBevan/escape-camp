package me.elgregos.escapecamp.game.domain.event

import me.elgregos.escapecamp.config.exception.GameException.*
import me.elgregos.escapecamp.game.domain.entity.Contestant
import me.elgregos.escapecamp.game.domain.entity.EnrollmentType
import me.elgregos.escapecamp.game.domain.entity.Game
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

    fun createGame(enrollmentType: EnrollmentType, riddles: List<Pair<String, String>>, startedAt: LocalDateTime): Flux<GameEvent> =
        Flux.just(GameCreated(gameId, userId, startedAt, enrollmentType, riddles))

    fun enrollContestant(contestant: Contestant, enrolledAt: LocalDateTime): Flux<GameEvent> =
        previousState()
            .filter { !it.isEmpty }
            .switchIfEmpty(Mono.error { GameNotFoundException(gameId) })
            .map { JsonConvertible.fromJson(it, Game::class.java) }
            .filter { game -> game.contestantNameAvailable(contestant.name) }
            .switchIfEmpty(Mono.error { ContestantNameNotAvailableException(contestant.name) })
            .filter(Game::contestantLimitNotReached)
            .switchIfEmpty(Mono.error { ContestantNumberLimitExceededException() })
            .map { game -> game.enrollContestant(contestant, enrolledAt) }
            .flatMapMany { game ->
                nextVersion()
                    .map { version -> ContestantEnrolled(gameId, version, userId, enrolledAt, game.contestants) }
                    .flatMap { this.applyNewEvent(it) }
                    .concatWith(
                        nextVersion()
                            .filter { game.ableToStartAutomatically() }
                            .map { version -> GameStarted(gameId, version, userId, enrolledAt) }
                    )
            }

    fun assignContestantNextRiddle(assignedAt: LocalDateTime): Flux<GameEvent> =
        previousState()
            .filter { !it.isEmpty }
            .switchIfEmpty(Mono.error { GameNotFoundException(gameId) })
            .map { JsonConvertible.fromJson(it, Game::class.java) }
            .filter { game -> game.checkIfContestantExists(userId) }
            .switchIfEmpty(Mono.error { ContestantNotFoundException(userId) })
            .filter { game -> game.contestants.size == game.riddles.size }
            .switchIfEmpty(Mono.error { GameNotStartedException() })
            .filter { game -> game.canAssignRiddleToContestant(userId) }
            .switchIfEmpty(Mono.error { PreviousRiddleNotSolvedException() })
            .map { game -> game.assignRiddleToContestant(userId, assignedAt) }
            .flatMapMany { game ->
                nextVersion()
                    .map { nextVersion -> NextContestantRiddleAssigned(gameId, nextVersion, assignedAt, userId, game.contestants) }
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
            .filter { game -> game.contestantLastUnsolvedRiddle(userId)?.name == riddleName }
            .switchIfEmpty(Mono.error { UnexpectedRiddleSolutionException(riddleName) })
            .map { game -> game.solveLastAssignedRiddleOfContestant(userId, submittedAt) }
            .flatMapMany { game ->
                nextVersion()
                    .map { nextVersion -> RiddleSolved(gameId, nextVersion, submittedAt, userId, game.contestants) }
                    .flatMap { this.applyNewEvent(it) }
                    .cast(GameEvent::class.java)
                    .concatWith(nextVersion()
                        .filter { game.checkIfIsFirstContestantToSolveAllRiddle() }
                        .map { version -> WinnerAnnounced(gameId, version, submittedAt, userId) })
            }
}
