package me.elgregos.escapecamp.game.domain.event

import me.elgregos.escapecamp.game.domain.entity.*
import me.elgregos.escapecamp.game.domain.event.GameEvent.GameCreated
import me.elgregos.escapecamp.game.domain.event.GameEvent.TeamAdded
import me.elgregos.reakteves.libs.genericObjectMapper
import java.time.LocalDateTime


val escapeCampCreated = GameCreated(
    aggregateId = escapeCampId,
    createdAt = escapeCampCreatedAt,
    createdBy = escapeCampCreatorId,
    event = genericObjectMapper.createObjectNode().put("id", "$escapeCampId").put("createdAt", "$escapeCampCreatedAt")
)

val lockedAndLoadedTeamAddAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 30)

val lockedAndLoadedTeamAdded = TeamAdded(
    gameId = escapeCampId,
    createdAt = lockedAndLoadedTeamAddAt,
    createdBy = lockedAndLoadedTeamId,
    event = genericObjectMapper.createObjectNode()
        .put("updatedBy", "0bfce65c-dff9-4f2e-8e8f-11ed6151b205")
        .put("updatedAt", "2023-06-15T13:30")
        .set(
            "teams",
            genericObjectMapper.createArrayNode()
                .add(lockedAndLoadedTeam.toJson())
        ),
)

//val escapeCampStarted = GameStarted(gameId = escapeCampId, createdAt = escapeCampCreatedAt, createdBy =  escapeCampCreatorId)
