package me.elgregos.escapecamp.game.application

import me.elgregos.escapecamp.game.domain.repository.GameRepository
import me.elgregos.escapecamp.game.domain.service.RiddleSolutionChecker
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GameService(
    private val gameRepository: GameRepository,
    override val riddles: List<Pair<String, String>>): RiddleSolutionChecker {

    fun retrieveRiddleContent(riddleName: String): String =
        GameService::class.java.getResource("/riddles/$riddleName.md")
            ?.readText()!!

    fun games() =
        gameRepository.list()

    fun game(gameId: UUID) =
        gameRepository.find(gameId)

}