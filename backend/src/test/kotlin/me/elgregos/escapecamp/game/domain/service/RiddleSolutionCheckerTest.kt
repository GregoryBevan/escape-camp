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
            # Episode I

            Un mot à trouver.

            Pour avoir le début, il faut se rappeler la stratégie de discipline de Monsieur Rachin dans _Les Choristes_ : « action, \_\_\_\_\_ion ».

            Le mot rime avec le prénom du chanteur et acteur franco-italien qui a joué dans _La Folie des grandeurs_.

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
            # Episode I

            Un mot à trouver.

            Pour avoir le début, il faut se rappeler la stratégie de discipline de Monsieur Rachin dans _Les Choristes_ : « action, \_\_\_\_\_ion ».

            Le mot rime avec le prénom du chanteur et acteur franco-italien qui a joué dans _La Folie des grandeurs_.

            """.trimIndent())
    }

}
