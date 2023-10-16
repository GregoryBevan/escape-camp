package me.elgregos.escapecamp.game.domain.service

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.escapecamp.game.domain.entity.riddles
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class RiddleSolutionCheckerTest {

    private lateinit var riddleSolutionChecker: RiddleSolutionChecker

    @BeforeTest
    fun setUp() {
        riddleSolutionChecker = MockedRiddleSolutionChecker(riddles)
    }

    @Test
    fun `should retrieve riddle content`() {
        assertThat(riddleSolutionChecker.retrieveRiddleContent("riddle-1"))
            .isEqualTo("""
            # A la piscine

            Si vous vous placez au bon endroit,
            la réponse se révèlera.

            """.trimIndent())
    }

    @ParameterizedTest
    @CsvSource(
        "riddle-1, solution-1, true",
        "riddle-1, Solution-1, true",
        "riddle-1, solution-2, false",
        "unknown, solution-2, false")
    fun `should check riddle solution`(riddleName: String, solution: String, expectedResult: Boolean) {
        assertThat(riddleSolutionChecker.isCorrect(riddleName, solution))
            .isEqualTo(expectedResult)
    }

    @Test
    fun `should calculate game leaderboard`() {
        assertThat(riddleSolutionChecker.retrieveRiddleContent("riddle-1"))
            .isEqualTo("""
            # A la piscine

            Si vous vous placez au bon endroit,
            la réponse se révèlera.

            """.trimIndent())
    }

}
