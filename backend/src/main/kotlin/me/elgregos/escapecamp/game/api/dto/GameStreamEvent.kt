package me.elgregos.escapecamp.game.api.dto

import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.escapecamp.game.domain.event.GameEvent

data class GameStreamEvent(val id: String, val gameId: String, val eventType: String, val data: JsonNode)

fun fromGameEvent(gameEvent: GameEvent) =
    GameStreamEvent(gameEvent.id.toString(),gameEvent.aggregateId.toString(), gameEvent.eventType, gameEvent.event)