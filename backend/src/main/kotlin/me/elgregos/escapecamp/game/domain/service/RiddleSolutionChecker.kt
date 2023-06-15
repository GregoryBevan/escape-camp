package me.elgregos.escapecamp.game.domain.service

interface RiddleSolutionChecker {

    val riddles: List<Pair<String, String>>

    fun isCorrect(riddleName: String, solution: String): Boolean =
        riddles.find { it.first == riddleName }
            ?.let { it.second == solution }
            ?: false
}