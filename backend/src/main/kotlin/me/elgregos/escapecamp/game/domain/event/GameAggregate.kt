package me.elgregos.escapecamp.game.domain.event

import me.elgregos.reakteves.domain.EventStore
import me.elgregos.reakteves.domain.JsonAggregate
import me.elgregos.reakteves.libs.genericObjectMapper
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.*

class GameAggregate(private val gameId: UUID, private val userId: UUID, eventStore: EventStore<GameEvent, UUID>) :
    JsonAggregate<GameEvent, UUID>(gameId, eventStore) {

    fun createGame(startedAt: LocalDateTime): Mono<GameEvent> =
        Mono.just(GameEvent.GameCreated(aggregateId = gameId, createdBy = userId, createdAt = startedAt, event = genericObjectMapper.createObjectNode()))

}
