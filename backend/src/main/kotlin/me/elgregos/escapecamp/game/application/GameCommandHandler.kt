package me.elgregos.escapecamp.game.application

import me.elgregos.escapecamp.game.application.GameCommand.*
import me.elgregos.escapecamp.game.domain.event.GameAggregate
import me.elgregos.escapecamp.game.domain.event.GameEvent
import me.elgregos.reakteves.domain.event.EventStore
import me.elgregos.reakteves.infrastructure.event.ReactorEventPublisher
import org.springframework.stereotype.Service
import java.util.*

@Service
class GameCommandHandler(
    val gameService: GameService,
    val gameEventStore: EventStore<GameEvent, UUID, UUID>,
    val eventPublisher: ReactorEventPublisher<UUID, UUID>
) {

    fun handle(gameCommand: GameCommand) =
        when (gameCommand) {
            is CreateGame -> createGame(gameCommand)
            is EnrollContestant -> enrollContestant(gameCommand)
            is AssignContestantNextRiddle -> assignContestantNextRiddle(gameCommand)
            is SubmitRiddleSolution -> checkRiddleSolution(gameCommand)
        }
            .flatMap { gameEventStore.save(it) }
            .doOnNext { eventPublisher.publish(it) }

    private fun createGame(gameCommand: CreateGame) =
        GameAggregate(gameCommand.gameId, gameCommand.createdBy, gameService, gameEventStore)
            .createGame(gameCommand.riddles, gameCommand.createdAt)

    private fun enrollContestant(gameCommand: EnrollContestant) =
        GameAggregate(gameCommand.gameId, gameCommand.enrolledBy, gameService, gameEventStore)
            .enrollContestant(gameCommand.contestant, gameCommand.enrolledAt, gameCommand.limitContestants)

    private fun assignContestantNextRiddle(gameCommand: AssignContestantNextRiddle) =
        GameAggregate(gameCommand.gameId, gameCommand.assignedBy, gameService, gameEventStore)
            .assignContestantNextRiddle(gameCommand.assignedAt)

    private fun checkRiddleSolution(gameCommand: SubmitRiddleSolution) =
        GameAggregate(gameCommand.gameId, gameCommand.submittedBy, gameService, gameEventStore)
            .checkRiddleSolution(gameCommand.riddleName, gameCommand.solution, gameCommand.submittedAt)

}
