package me.elgregos.escapecamp.game.domain.event

import me.elgregos.escapecamp.game.domain.event.GameEvent.GameCreated
import me.elgregos.reakteves.libs.genericObjectMapper
import java.time.LocalDateTime
import java.util.*

val escapeCampId: UUID = UUID.fromString("981e1b04-ecc6-48b3-b750-58f20faa5e05")
val escapeCampCreatorId: UUID = UUID.fromString("b4ad63e8-f221-4c71-8e11-cf30e808baf0")
val escapeCampCreatedAt: LocalDateTime = LocalDateTime.of(2023, 5, 19, 21, 40, 18)

val escapeCampCreated = GameCreated(aggregateId = escapeCampId, createdAt = escapeCampCreatedAt, createdBy =  escapeCampCreatorId, event = genericObjectMapper.createObjectNode())

//val escapeCampStarted = GameStarted(gameId = escapeCampId, createdAt = escapeCampCreatedAt, createdBy =  escapeCampCreatorId)

//fun teamAddedEvent(gameId: UUID, team: Team) = TeamAdded(
//    gameId = gameId,
//    createdBy = team.id,
//    team = team
//)