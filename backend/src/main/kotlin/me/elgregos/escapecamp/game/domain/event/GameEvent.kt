package me.elgregos.escapecamp.game.domain.event

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.readValue
import me.elgregos.escapecamp.game.domain.entity.Riddle
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
        override val event: JsonNode
    ) : GameEvent(
        id,
        sequenceNum,
        version,
        createdAt,
        createdBy,
        gameId,
        GameCreated::class.simpleName!!,
        event
    ) {

        constructor(gameId: UUID, createdBy: UUID, createdAt: LocalDateTime, riddles: List<Pair<String, String>>) : this(
            gameId = gameId,
            createdAt = createdAt,
            createdBy = createdBy,
            event = genericObjectMapper.createObjectNode()
                .put("id", "$gameId")
                .put("createdAt", "$createdAt")
                .put("createdBy", "$createdBy")
                .set("riddles", genericObjectMapper.valueToTree(riddles)))
    }

    data class TeamAdded(
        override val id: UUID = UUID.randomUUID(),
        override val sequenceNum: Long? = null,
        override val version: Int = 1,
        val addedAt: LocalDateTime = nowUTC(),
        val addedBy: UUID,
        val gameId: UUID,
        override val event: JsonNode,
    ) : GameEvent(
        id,
        sequenceNum,
        version,
        addedAt,
        addedBy,
        gameId,
        TeamAdded::class.simpleName!!,
        event
    ) {
        constructor(gameId: UUID, version: Int, addedBy: UUID, addedAt: LocalDateTime, teams: List<Team>) : this(
            gameId = gameId,
            version = version,
            addedBy = addedBy,
            addedAt = addedAt,
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
        val startedAt: LocalDateTime = nowUTC(),
        val startedBy: UUID,
        val gameId: UUID,
        override val event: JsonNode
    ) : GameEvent(
        id,
        sequenceNum,
        version,
        startedAt,
        startedBy,
        gameId,
        GameStarted::class.simpleName!!,
        event
    ) {
        constructor(gameId: UUID, version: Int, startedBy: UUID, startedAt: LocalDateTime): this(
            gameId = gameId,
            version = version,
            startedBy = startedBy,
            startedAt = startedAt,
            event = genericObjectMapper.createObjectNode()
                .put("id", "$gameId")
                .put("startedAt", "$startedAt")
        )
    }

    data class NextTeamRiddleAssigned(
        override val id: UUID = UUID.randomUUID(),
        override val sequenceNum: Long? = null,
        override val version: Int,
        val assignedAt: LocalDateTime = nowUTC(),
        val assignedBy: UUID,
        val gameId: UUID,
        override val event: JsonNode
    ): GameEvent(
        id,
        sequenceNum,
        version,
        assignedAt,
        assignedBy,
        gameId,
        NextTeamRiddleAssigned::class.simpleName!!,
        event
    ) {

        constructor(gameId: UUID, version: Int, assignedAt: LocalDateTime, assignedBy: UUID, teams: List<Team>) : this(
            gameId = gameId,
            version = version,
            assignedAt = assignedAt,
            assignedBy = assignedBy,
            event = genericObjectMapper.createObjectNode()
                .put("id", "$gameId")
                .put("updatedAt", "$assignedAt")
                .put("updatedBy", "$assignedBy")
                .set("teams", genericObjectMapper.valueToTree(teams))
        )

        fun assignedRiddle(): Riddle =
            genericObjectMapper.readValue<List<Team>>(event.get("teams").toString())
                .find { team -> team.id == assignedBy }!!
                .lastUnsolvedRiddle()
    }

    data class RiddleSolved(
        override val id: UUID = UUID.randomUUID(),
        override val sequenceNum: Long? = null,
        override val version: Int,
        val solvedAt: LocalDateTime = nowUTC(),
        val solvedBy: UUID,
        val gameId: UUID,
        override val event: JsonNode
    ): GameEvent(
        id,
        sequenceNum,
        version,
        solvedAt,
        solvedBy,
        gameId,
        RiddleSolved::class.simpleName!!,
        event
    ) {

        constructor(gameId: UUID, version: Int, solvedAt: LocalDateTime, solvedBy: UUID, teams: List<Team>) : this(
            gameId = gameId,
            version = version,
            solvedAt = solvedAt,
            solvedBy = solvedBy,
            event = genericObjectMapper.createObjectNode()
                .put("id", "$gameId")
                .put("updatedAt", "$solvedAt")
                .put("updatedBy", "$solvedBy")
                .set("teams", genericObjectMapper.valueToTree(teams))
        )
    }

    data class WinnerDefined(
        override val id: UUID = UUID.randomUUID(),
        override val sequenceNum: Long? = null,
        override val version: Int,
        val definedAt: LocalDateTime = nowUTC(),
        val definedBy: UUID,
        val gameId: UUID,
        override val event: JsonNode
    ): GameEvent(
        id,
        sequenceNum,
        version,
        definedAt,
        definedBy,
        gameId,
        WinnerDefined::class.simpleName!!,
        event
    ) {

        constructor(gameId: UUID, version: Int, definedAt: LocalDateTime, teamId: UUID) : this(
            gameId = gameId,
            version = version,
            definedAt = definedAt,
            definedBy = teamId,
            event = genericObjectMapper.createObjectNode()
                .put("id", "$gameId")
                .put("winner", "$teamId")
        )
    }
}