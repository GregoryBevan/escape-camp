package me.elgregos.escapecamp.game.domain.repository

import me.elgregos.escapecamp.game.domain.entity.Game
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

interface GameRepository {

    fun list(): Flux<Game>

    fun find(gameId: UUID): Mono<Game>

    fun insert(game: Game): Mono<Game>

    fun update(game: Game): Mono<Game>
}