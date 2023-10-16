package me.elgregos.escapecamp.game.infrastructure.event

import jakarta.annotation.PostConstruct
import me.elgregos.escapecamp.config.sse.ServerSentEventService
import me.elgregos.escapecamp.game.api.dto.fromGameEvent
import me.elgregos.escapecamp.game.domain.event.GameEvent
import me.elgregos.reakteves.domain.event.Event
import me.elgregos.reakteves.infrastructure.event.ReactorEventBus
import me.elgregos.reakteves.infrastructure.event.ReactorEventSubscriber
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

@Component
class GameServerSentEventSubscriber(
    private val serverSentEventService: ServerSentEventService,
    reactorEventBus: ReactorEventBus<UUID, UUID>
) :
    ReactorEventSubscriber<UUID, UUID>(reactorEventBus) {

    @PostConstruct
    fun initialize() = subscribe()

    override fun onEvent(event: Event<UUID, UUID>): Mono<Void> =
        Mono.just(event)
            .filter { it is GameEvent }
            .cast(GameEvent::class.java)
            .doOnNext { gameEvent: GameEvent ->
                if(gameEvent is GameEvent.GameCreated) serverSentEventService.heartBeat(gameEvent.gameId, gameEvent.createdAt)
                serverSentEventService.sseEmit(fromGameEvent(gameEvent))

            }
            .then()

}
