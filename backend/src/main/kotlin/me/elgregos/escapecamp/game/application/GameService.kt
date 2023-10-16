package me.elgregos.escapecamp.game.application

import me.elgregos.escapecamp.game.domain.entity.Game
import me.elgregos.escapecamp.game.domain.entity.LeaderBoard
import me.elgregos.escapecamp.game.domain.service.RiddleSolutionChecker
import me.elgregos.reakteves.domain.projection.ProjectionStore
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class GameService(
    private val gameProjectionStore: ProjectionStore<Game, UUID, UUID>,
    override val riddles: List<Pair<String, String>>): RiddleSolutionChecker {

    fun games() =
        gameProjectionStore.list()

    fun game(gameId: UUID) =
        gameProjectionStore.find(gameId)

    fun gameLeaderBoard(gameId: UUID): Mono<LeaderBoard> =
        gameProjectionStore.find(gameId)
            .map { game -> LeaderBoard.ofGame(game) }

}
