package me.elgregos.escapecamp.config.sse

import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.escapecamp.game.api.dto.GameStreamEvent
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Service
import reactor.core.publisher.Sinks

@Service
class ServerSentEventService(private val streamEventBus: Sinks.Many<ServerSentEvent<JsonNode>>) {

    fun sseEmit(gameStreamEvent: GameStreamEvent) = streamEventBus.emitNext(
        ServerSentEvent.builder<JsonNode>()
            .id(gameStreamEvent.id)
            .event(gameStreamEvent.eventType)
            .data(gameStreamEvent.data)
            .build(), Sinks.EmitFailureHandler.FAIL_FAST
    )

    fun sseFlux() = streamEventBus.asFlux()
}