package me.elgregos.escapecamp.game.application

import org.springframework.stereotype.Service

@Service
class RiddleService {

    fun retrieveRiddleContent(riddleName: String): String =
        RiddleService::class.java.getResource("/riddles/$riddleName.md")
            ?.readText()!!

}