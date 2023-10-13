package me.elgregos.escapecamp.config.game

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class GameConfig {

    @Bean
    fun riddles(gameProperties: GameProperties): List<Pair<String, String>> =
        gameProperties.riddles
            .flatMap { it.toList() }
            .map { Pair(it.first.replace(".solution", ""), it.second)}
}

@ConfigurationProperties(prefix = "game")
data class GameProperties @ConstructorBinding constructor(
    val limitContestants: Boolean,
    val riddles: List<Map<String, String>>
)
