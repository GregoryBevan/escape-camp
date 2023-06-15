package me.elgregos.escapecamp.game.infrastructure.projection

import me.elgregos.escapecamp.game.domain.entity.Game
import me.elgregos.escapecamp.game.domain.repository.GameRepository
import org.springframework.data.domain.Sort
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

interface GameProjectionRepository : GameRepository, R2dbcRepository<GameEntity, UUID> {

    override fun list(): Flux<Game> =
        findAll(Sort.by("sequence_num"))
            .map(GameEntity::toGame)

    override fun find(gameId: UUID): Mono<Game> =
        findById(gameId)
            .map(GameEntity::toGame)

    override fun insert(game: Game): Mono<Game> =
        save(GameEntity.fromGame(game).apply(GameEntity::markNew))
            .map(GameEntity::toGame)

    override fun update(game: Game, sequenceNum: Long): Mono<Game> =
        save(GameEntity.fromGame(game).copy(sequenceNum = sequenceNum))
            .map(GameEntity::toGame)

}