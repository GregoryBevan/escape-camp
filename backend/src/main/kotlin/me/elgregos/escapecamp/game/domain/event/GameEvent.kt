package me.elgregos.escapecamp.game.domain.event

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.readValue
import me.elgregos.escapecamp.game.domain.entity.Riddle
import me.elgregos.escapecamp.game.domain.entity.Contestant
import me.elgregos.escapecamp.game.domain.entity.EnrollmentType
import me.elgregos.reakteves.domain.event.Event
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
) : Event<UUID, UUID>(
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

        constructor(gameId: UUID, createdBy: UUID, createdAt: LocalDateTime, enrollmentType: EnrollmentType, riddles: List<Pair<String, String>>) : this(
            gameId = gameId,
            createdAt = createdAt,
            createdBy = createdBy,
            event = genericObjectMapper.createObjectNode()
                .put("id", "$gameId")
                .put("createdAt", "$createdAt")
                .put("createdBy", "$createdBy")
                .put("enrollmentType", enrollmentType.name)
                .set("riddles", genericObjectMapper.valueToTree(riddles)))
    }

    data class ContestantEnrolled(
        override val id: UUID = UUID.randomUUID(),
        override val sequenceNum: Long? = null,
        override val version: Int = 1,
        val enrolledAt: LocalDateTime = nowUTC(),
        val enrolledBy: UUID,
        val gameId: UUID,
        override val event: JsonNode,
    ) : GameEvent(
        id,
        sequenceNum,
        version,
        enrolledAt,
        enrolledBy,
        gameId,
        ContestantEnrolled::class.simpleName!!,
        event
    ) {
        constructor(gameId: UUID, version: Int, enrolledBy: UUID, enrolledAt: LocalDateTime, contestants: List<Contestant>) : this(
            gameId = gameId,
            version = version,
            enrolledBy = enrolledBy,
            enrolledAt = enrolledAt,
            event = genericObjectMapper.createObjectNode()
                .put("id", "$gameId")
                .put("updatedBy", "$enrolledBy")
                .put("updatedAt", "$enrolledAt")
                .set("contestants", genericObjectMapper.valueToTree(contestants))
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

    data class NextRiddleUnlocked(
        override val id: UUID = UUID.randomUUID(),
        override val sequenceNum: Long? = null,
        override val version: Int,
        val unlockedAt: LocalDateTime = nowUTC(),
        val unlockedBy: UUID,
        val gameId: UUID,
        override val event: JsonNode
    ): GameEvent(
        id,
        sequenceNum,
        version,
        unlockedAt,
        unlockedBy,
        gameId,
        NextRiddleUnlocked::class.simpleName!!,
        event
    ) {

        constructor(gameId: UUID, version: Int, unlockedAt: LocalDateTime, unlockedBy: UUID, unlockedRiddle: Int) : this(
            gameId = gameId,
            version = version,
            unlockedAt = unlockedAt,
            unlockedBy = unlockedBy,
            event = genericObjectMapper.createObjectNode()
                .put("id", "$gameId")
                .put("updatedAt", "$unlockedAt")
                .put("updatedBy", "$unlockedBy")
                .put("currentRiddle", unlockedRiddle)
        )

        fun currentRiddle(): Int = event["currentRiddle"].asInt()
    }

    data class NextContestantRiddleAssigned(
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
        NextContestantRiddleAssigned::class.simpleName!!,
        event
    ) {

        constructor(gameId: UUID, version: Int, assignedAt: LocalDateTime, assignedBy: UUID, contestants: List<Contestant>) : this(
            gameId = gameId,
            version = version,
            assignedAt = assignedAt,
            assignedBy = assignedBy,
            event = genericObjectMapper.createObjectNode()
                .put("id", "$gameId")
                .put("updatedAt", "$assignedAt")
                .put("updatedBy", "$assignedBy")
                .set("contestants", genericObjectMapper.valueToTree(contestants))
        )

        fun assignedRiddle(): Riddle =
            genericObjectMapper.readValue<List<Contestant>>(event.get("contestants").toString())
                .find { contestant -> contestant.id == assignedBy }!!
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

        constructor(gameId: UUID, version: Int, solvedAt: LocalDateTime, solvedBy: UUID, contestants: List<Contestant>) : this(
            gameId = gameId,
            version = version,
            solvedAt = solvedAt,
            solvedBy = solvedBy,
            event = genericObjectMapper.createObjectNode()
                .put("id", "$gameId")
                .put("updatedAt", "$solvedAt")
                .put("updatedBy", "$solvedBy")
                .set("contestants", genericObjectMapper.valueToTree(contestants))
        )
    }

    data class WinnerAnnounced(
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
        WinnerAnnounced::class.simpleName!!,
        event
    ) {

        constructor(gameId: UUID, version: Int, definedAt: LocalDateTime, contestantId: UUID) : this(
            gameId = gameId,
            version = version,
            definedAt = definedAt,
            definedBy = contestantId,
            event = genericObjectMapper.createObjectNode()
                .put("id", "$gameId")
                .put("winner", "$contestantId")
        )
    }
}
