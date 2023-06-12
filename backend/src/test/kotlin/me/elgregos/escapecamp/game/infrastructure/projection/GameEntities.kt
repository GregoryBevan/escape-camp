package me.elgregos.escapecamp.game.infrastructure.projection

import me.elgregos.escapecamp.game.domain.entity.escapeCamp
import me.elgregos.escapecamp.game.domain.entity.escapeCampCreatedAt
import me.elgregos.escapecamp.game.domain.entity.escapeCampId

val escapeCampEntity = GameEntity(
    escapeCampId,
    null,
    1,
    escapeCampCreatedAt,
    escapeCampCreatedAt,
    escapeCamp.toJson()
)
