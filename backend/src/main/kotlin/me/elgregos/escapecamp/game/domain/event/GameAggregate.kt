package me.elgregos.escapecamp.game.domain.event

import me.elgregos.escapecamp.config.exception.GameException
import me.elgregos.escapecamp.game.domain.entity.Game
import me.elgregos.escapecamp.game.domain.entity.Team
import me.elgregos.reakteves.domain.EventStore
import me.elgregos.reakteves.domain.JsonAggregate
import me.elgregos.reakteves.domain.JsonConvertible
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.*

class GameAggregate(private val gameId: UUID, private val userId: UUID, eventStore: EventStore<GameEvent, UUID>) :
    JsonAggregate<GameEvent, UUID>(gameId, eventStore) {

    fun createGame(startedAt: LocalDateTime): Mono<GameEvent> =
        Mono.just(GameEvent.GameCreated(aggregateId = gameId, createdBy = userId, createdAt = startedAt))

    fun addTeam(team: Team, addedAt: LocalDateTime): Mono<GameEvent> =
        previousState()
            .map { JsonConvertible.fromJson(it, Game::class.java) }
            .filter { game -> game.isTeamNameAvailable(team.name) }
            .switchIfEmpty(Mono.error { GameException.TeamNameNotAvailableException(team.name) })
            .filter { game -> game.teams.size < 4 }
            .switchIfEmpty(Mono.error { GameException.TeamNumberLimitExceededException() })
            .map { game -> game.addTeam(team) }
            .map { game -> GameEvent.TeamAdded(gameId, userId, addedAt, game.teams) }


}
