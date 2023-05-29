package me.elgregos.escapecamp.game.application

import me.elgregos.escapecamp.game.application.GameCommand.AddTeam
import me.elgregos.escapecamp.game.application.GameCommand.CreateGame
import me.elgregos.escapecamp.game.domain.event.GameAggregate
import me.elgregos.escapecamp.game.domain.event.GameEvent
import me.elgregos.reakteves.domain.EventStore
import me.elgregos.reakteves.infrastructure.ReactorEventPublisher
import org.springframework.stereotype.Service
import java.util.*

@Service
class GameCommandHandler(
    val gameEventStore: EventStore<GameEvent, UUID>,
    val eventPublisher: ReactorEventPublisher<UUID>
) {

    fun handle(gameCommand: GameCommand) =
        when (gameCommand) {
            is CreateGame -> createGame(gameCommand)
            is AddTeam -> addTeam(gameCommand)
        }
            .flatMap { gameEventStore.save(it) }
            .doOnNext { eventPublisher.publish(it) }

    private fun createGame(gameCommand: CreateGame) =
        GameAggregate(gameCommand.gameId, gameCommand.createdBy, gameEventStore)
            .createGame(gameCommand.createdAt)

    private fun addTeam(gameCommand: AddTeam) =
        GameAggregate(gameCommand.gameId, gameCommand.addedBy, gameEventStore)
            .createGame(gameCommand.addedAt)

}