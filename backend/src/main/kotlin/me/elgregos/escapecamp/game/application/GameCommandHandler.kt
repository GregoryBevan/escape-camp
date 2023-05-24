package me.elgregos.escapecamp.game.application

import me.elgregos.escapecamp.game.domain.event.GameAggregate
import me.elgregos.escapecamp.game.domain.event.GameEvent
import me.elgregos.reakteves.domain.EventStore
import me.elgregos.reakteves.infrastructure.ReactorEventPublisher
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GameCommandHandler(
    val gameEventStore: EventStore<GameEvent, UUID>,
    val eventPublisher: ReactorEventPublisher<UUID>
) {

    fun handle(gameCommand: GameCommand) =
        when (gameCommand) {
            is GameCommand.CreateGame -> handleCreateGame(gameCommand)
        }
            .flatMap { gameEventStore.save(it) }
            .doOnNext { eventPublisher.publish(it) }

    private fun handleCreateGame(gameCommand: GameCommand.CreateGame) =
        GameAggregate(gameCommand.gameId, gameCommand.createdBy, gameEventStore)
            .createGame(gameCommand.createdAt)

}