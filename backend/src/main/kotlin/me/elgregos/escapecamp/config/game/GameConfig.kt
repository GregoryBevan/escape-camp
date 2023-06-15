package me.elgregos.escapecamp.config.game

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class GameConfig {

    @Bean
    fun riddles(riddleConfig: RiddleConfig): List<Pair<String, String>> =
        riddleConfig.riddles
            .flatMap { it.toList() }
            .map { Pair(it.first.replace(".solution", ""), it.second)}
}

@ConfigurationProperties(prefix = "game")
data class RiddleConfig @ConstructorBinding constructor(
    val riddles: List<Map<String, String>>
)