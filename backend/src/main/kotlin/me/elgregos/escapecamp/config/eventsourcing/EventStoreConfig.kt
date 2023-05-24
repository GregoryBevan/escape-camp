package me.elgregos.escapecamp.config.eventsourcing

import me.elgregos.escapecamp.game.domain.event.GameEvent
import me.elgregos.escapecamp.game.domain.event.GameEventRepository
import me.elgregos.escapecamp.game.infrastructure.event.GameEventEntity
import me.elgregos.reakteves.domain.EventStore
import me.elgregos.reakteves.infrastructure.DefaultEventStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.UUID

@Configuration
class EventStoreConfig {

    @Bean
    fun gameEventStore(gameEventRepository: GameEventRepository): EventStore<GameEvent, UUID> =
        DefaultEventStore(gameEventRepository, GameEventEntity::class, GameEvent::class)
}