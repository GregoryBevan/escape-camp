package me.elgregos.escapecamp.game.application

import me.elgregos.escapecamp.game.domain.repository.GameRepository
import me.elgregos.escapecamp.game.domain.service.RiddleSolutionChecker
import org.springframework.stereotype.Service

@Service
class GameService(private val gameRepository: GameRepository): RiddleSolutionChecker {

    fun retrieveRiddleContent(riddleName: String): String =
        GameService::class.java.getResource("/riddles/$riddleName.md")
            ?.readText()!!

}