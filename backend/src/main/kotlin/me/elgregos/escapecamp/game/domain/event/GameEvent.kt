package me.elgregos.escapecamp.game.domain.event

import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.escapecamp.game.domain.entity.Team
import me.elgregos.reakteves.domain.Event
import me.elgregos.reakteves.libs.genericObjectMapper
import me.elgregos.reakteves.libs.nowUTC
import java.time.LocalDateTime
import java.util.*

sealed class GameEvent(
    id: UUID,
    sequenceNum: Long?,
    version: Int,
    createdAt: LocalDateTime,
    createdBy: UUID,
    aggregateId: UUID,
    eventType: String,
    event: JsonNode
) : Event<UUID>(
    id, sequenceNum, version, createdAt, createdBy, eventType, aggregateId, event
) {

    data class GameCreated(
        override val id: UUID = UUID.randomUUID(),
        override val sequenceNum: Long? = null,
        override val version: Int = 1,
        override val createdAt: LocalDateTime = nowUTC(),
        override val createdBy: UUID,
        val gameId: UUID,
        override val event: JsonNode = genericObjectMapper.createObjectNode().put("id", "$gameId")
            .put("createdAt", "$createdAt")
            .put("createdBy", "$createdBy")
    ) : GameEvent(
        id,
        sequenceNum,
        version,
        createdAt,
        createdBy,
        gameId,
        GameCreated::class.simpleName!!,
        event
    )

    data class TeamAdded(
        override val id: UUID = UUID.randomUUID(),
        override val sequenceNum: Long? = null,
        override val version: Int = 1,
        override val createdAt: LocalDateTime = nowUTC(),
        override val createdBy: UUID,
        val gameId: UUID,
        override val event: JsonNode,
    ) : GameEvent(
        id,
        sequenceNum,
        version,
        createdAt,
        createdBy,
        gameId,
        TeamAdded::class.simpleName!!,
        event
    ) {
        constructor(gameId: UUID, version: Int, addedBy: UUID, addedAt: LocalDateTime, teams: List<Team>) : this(
            gameId = gameId,
            version = version,
            createdBy = addedBy,
            createdAt = addedAt,
            event = genericObjectMapper.createObjectNode()
                .put("id", "$gameId")
                .put("updatedBy", "$addedBy")
                .put("updatedAt", "$addedAt")
                .set("teams", genericObjectMapper.valueToTree(teams))
        )
    }

    data class GameStarted(
        override val id: UUID = UUID.randomUUID(),
        override val sequenceNum: Long? = null,
        override val version: Int,
        override val createdAt: LocalDateTime = nowUTC(),
        override val createdBy: UUID,
        val gameId: UUID,
        override val event: JsonNode = genericObjectMapper.createObjectNode()
            .put("id", "$gameId")
            .put("startedAt", "$createdAt"),
    ) : GameEvent(
        id,
        sequenceNum,
        version,
        createdAt,
        createdBy,
        gameId,
        GameStarted::class.simpleName!!,
        event
    )
}