package me.elgregos.escapecamp.game.infrastructure.event

import jakarta.annotation.PostConstruct
import me.elgregos.escapecamp.config.sse.ServerSentEventService
import me.elgregos.escapecamp.game.api.dto.fromGameEvent
import me.elgregos.escapecamp.game.domain.event.GameEvent
import me.elgregos.reakteves.domain.Event
import me.elgregos.reakteves.infrastructure.ReactorEventBus
import me.elgregos.reakteves.infrastructure.ReactorEventSubscriber
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

@Component
class GameServerSentEventSubscriber(
    private val serverSentEventService: ServerSentEventService,
    reactorEventBus: ReactorEventBus<UUID>
) :
    ReactorEventSubscriber<UUID>(reactorEventBus) {

    @PostConstruct
    fun initialize() = subscribe()

    override fun onEvent(event: Event<UUID>): Mono<Void> =
        Mono.just(event)
            .filter { it is GameEvent }
            .cast(GameEvent::class.java)
            .doOnNext { gameEvent: GameEvent -> serverSentEventService.sseEmit(fromGameEvent(gameEvent)) }
            .then()

}