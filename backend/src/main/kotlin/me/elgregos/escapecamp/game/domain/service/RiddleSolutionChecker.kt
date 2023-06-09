package me.elgregos.escapecamp.game.domain.service

import me.elgregos.escapecamp.game.domain.entity.riddles

interface RiddleSolutionChecker {

    fun isCorrect(riddleName: String, solution: String): Boolean =
        riddles.find { it.first == riddleName }
            ?.let { it.second == solution }
            ?: false
}