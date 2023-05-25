package me.elgregos.escapecamp.game.domain.event

import me.elgregos.escapecamp.game.domain.entity.escapeCampCreatedAt
import me.elgregos.escapecamp.game.domain.entity.escapeCampCreatorId
import me.elgregos.escapecamp.game.domain.entity.escapeCampId
import me.elgregos.escapecamp.game.domain.event.GameEvent.GameCreated
import me.elgregos.reakteves.libs.genericObjectMapper


val escapeCampCreated = GameCreated(
    aggregateId = escapeCampId,
    createdAt = escapeCampCreatedAt,
    createdBy = escapeCampCreatorId,
    event = genericObjectMapper.createObjectNode().put("id", "$escapeCampId").put("createdAt", "$escapeCampCreatedAt")
)

//val escapeCampStarted = GameStarted(gameId = escapeCampId, createdAt = escapeCampCreatedAt, createdBy =  escapeCampCreatorId)

//fun teamAddedEvent(gameId: UUID, team: Team) = TeamAdded(
//    gameId = gameId,
//    createdBy = team.id,
//    team = team
//)