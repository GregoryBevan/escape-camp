package me.elgregos.escapecamp.game.domain.service

interface RiddleSolutionChecker {

    val riddles: List<Pair<String, String>>

    fun retrieveRiddleContent(riddleName: String): String =
        RiddleSolutionChecker::class.java.getResource("/riddles/$riddleName.md")
            ?.readText()!!

    fun isCorrect(riddleName: String, solution: String): Boolean =
        riddles.find { it.first == riddleName }
            ?.let { it.second.equals(solution, true) }
            ?: false
}
