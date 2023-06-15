package me.elgregos.escapecamp.config.sse

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ServerSentEvent
import reactor.core.publisher.Sinks

@Configuration
class ServerSentEventConfig {

    @Bean
    fun streamEventBus(): Sinks.Many<ServerSentEvent<JsonNode>> = Sinks.many().replay().all()

}