package me.elgregos.escapecamp.config.eventsourcing

import me.elgregos.reakteves.infrastructure.ReactorEventBus
import me.elgregos.reakteves.infrastructure.ReactorEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class EventBusConfig {

    @Bean
    fun reactorEventBus() = ReactorEventBus<UUID>()

    @Bean
    fun reactorEventPublisher(reactorEventBus: ReactorEventBus<UUID>): ReactorEventPublisher<UUID> =
        ReactorEventPublisher(reactorEventBus)

}