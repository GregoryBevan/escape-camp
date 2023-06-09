package me.elgregos.escapecamp.game.application

import me.elgregos.escapecamp.game.application.GameCommand.*
import me.elgregos.escapecamp.game.domain.event.GameAggregate
import me.elgregos.escapecamp.game.domain.event.GameEvent
import me.elgregos.reakteves.domain.EventStore
import me.elgregos.reakteves.infrastructure.ReactorEventPublisher
import org.springframework.stereotype.Service
import java.util.*

@Service
class GameCommandHandler(
    val riddleService: RiddleService,
    val gameEventStore: EventStore<GameEvent, UUID>,
    val eventPublisher: ReactorEventPublisher<UUID>,
) {

    fun handle(gameCommand: GameCommand) =
        when (gameCommand) {
            is CreateGame -> createGame(gameCommand)
            is AddTeam -> addTeam(gameCommand)
            is AssignTeamNextRiddle -> assignTeamNextRiddle(gameCommand)
            is SubmitRiddleSolution -> checkRiddleSolution(gameCommand)
        }
            .flatMap { gameEventStore.save(it) }
            .doOnNext { eventPublisher.publish(it) }

    private fun createGame(gameCommand: CreateGame) =
        GameAggregate(gameCommand.gameId, gameCommand.createdBy, riddleService, gameEventStore)
            .createGame(gameCommand.createdAt)

    private fun addTeam(gameCommand: AddTeam) =
        GameAggregate(gameCommand.gameId, gameCommand.addedBy, riddleService, gameEventStore)
            .addTeam(gameCommand.team, gameCommand.addedAt)

    private fun assignTeamNextRiddle(gameCommand: AssignTeamNextRiddle) =
        GameAggregate(gameCommand.gameId, gameCommand.assignedBy, riddleService, gameEventStore)
            .assignTeamNextRiddle(gameCommand.assignedAt)

    private fun checkRiddleSolution(gameCommand: SubmitRiddleSolution) =
        GameAggregate(gameCommand.gameId, gameCommand.submittedBy, riddleService, gameEventStore)
            .checkRiddleSolution(gameCommand.riddleName, gameCommand.solution, gameCommand.submittedAt)

}