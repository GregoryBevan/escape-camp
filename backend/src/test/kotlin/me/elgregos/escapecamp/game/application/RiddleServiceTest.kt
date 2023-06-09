package me.elgregos.escapecamp.game.application

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.BeforeTest
import kotlin.test.Test

class RiddleServiceTest {

    private lateinit var riddleService: RiddleService

    @BeforeTest
    fun setUp() {
        riddleService = RiddleService()
    }

    @Test
    fun `should retrieve riddle content`() {
        assertThat(riddleService.retrieveRiddleContent("riddle-1"))
            .isEqualTo("# First riddle")
    }

    @ParameterizedTest
    @CsvSource(
        "riddle-1, solution-1, true",
        "riddle-1, solution-2, false",
        "unknown, solution-2, false")
    fun `should check riddle solution`(riddleName: String, solution: String, expectedResult: Boolean) {
        assertThat(riddleService.isCorrect(riddleName, solution))
            .isEqualTo(expectedResult)
    }
}