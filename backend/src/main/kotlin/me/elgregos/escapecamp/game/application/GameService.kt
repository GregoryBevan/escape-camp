package me.elgregos.escapecamp.game.application

import me.elgregos.escapecamp.game.domain.entity.Game
import me.elgregos.escapecamp.game.domain.service.RiddleSolutionChecker
import me.elgregos.reakteves.domain.projection.ProjectionStore
import org.springframework.stereotype.Service
import java.util.*

@Service
class GameService(
    private val gameProjectionStore: ProjectionStore<Game, UUID, UUID>,
    override val riddles: List<Pair<String, String>>): RiddleSolutionChecker {

    fun retrieveRiddleContent(riddleName: String): String =
        GameService::class.java.getResource("/riddles/$riddleName.md")
            ?.readText()!!

    fun games() =
        gameProjectionStore.list()

    fun game(gameId: UUID) =
        gameProjectionStore.find(gameId)

}
