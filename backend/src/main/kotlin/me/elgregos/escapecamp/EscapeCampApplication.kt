package me.elgregos.escapecamp

import me.elgregos.escapecamp.config.game.RiddleConfig
import me.elgregos.escapecamp.config.security.jwt.JwtConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(JwtConfig::class, RiddleConfig::class)
class EscapeCampApplication

fun main(args: Array<String>) {
	runApplication<EscapeCampApplication>(*args)
}
