package me.elgregos.escapecamp.config.sse

import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.escapecamp.game.api.dto.GameStreamEvent
import me.elgregos.reakteves.libs.genericObjectMapper
import me.elgregos.reakteves.libs.nowUTC
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

@Service
class ServerSentEventService(
    private val streamEventBus: Sinks.Many<ServerSentEvent<JsonNode>>,
    @Value("\${game.sseHeartbeatPeriod:PT20S}") private val sseHeartbeatPeriod: Duration,
    @Value("\${game.maxDuration:PT90M}") private val gameMaxDuration: Duration
) {

    fun sseEmit(gameStreamEvent: GameStreamEvent) = streamEventBus.emitNext(
        ServerSentEvent.builder<JsonNode>()
            .id(gameStreamEvent.id)
            .event(gameStreamEvent.eventType)
            .data(gameStreamEvent.data)
            .build(), Sinks.EmitFailureHandler.FAIL_FAST
    )

    fun sseFlux() = streamEventBus.asFlux()

    fun heartBeat(gameId: UUID, createdAt: LocalDateTime) =
        Flux.just(UUID.randomUUID())
            .flatMap {
                Flux.interval(sseHeartbeatPeriod)
                    .map { interval ->
                        sseEmit(
                            GameStreamEvent(
                                "$it",
                                "$gameId",
                                "Heartbeat",
                                genericObjectMapper.createObjectNode()
                                    .put("id", "$gameId")
                                    .put("type" , "♥")
                            )
                        )
                    }
                    .takeUntil{ nowUTC().isAfter(createdAt.plus(gameMaxDuration)) }
            }
            .subscribe()
}

