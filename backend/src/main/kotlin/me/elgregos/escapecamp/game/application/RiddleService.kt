package me.elgregos.escapecamp.game.application

import me.elgregos.escapecamp.game.domain.service.RiddleSolutionChecker
import org.springframework.stereotype.Service

@Service
class RiddleService: RiddleSolutionChecker {

    fun retrieveRiddleContent(riddleName: String): String =
        RiddleService::class.java.getResource("/riddles/$riddleName.md")
            ?.readText()!!

}