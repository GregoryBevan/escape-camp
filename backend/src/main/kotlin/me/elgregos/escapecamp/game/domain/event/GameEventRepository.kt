package me.elgregos.escapecamp.game.domain.event

import me.elgregos.escapecamp.game.infrastructure.event.GameEventEntity
import me.elgregos.reakteves.infrastructure.EventEntityRepository
import java.util.*

interface GameEventRepository: EventEntityRepository<GameEventEntity, GameEvent, UUID>