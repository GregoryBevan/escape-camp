package me.elgregos.escapecamp.game.application

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.mockk
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.BeforeTest
import kotlin.test.Test

class GameServiceTest {

    private lateinit var gameService: GameService

    @BeforeTest
    fun setUp() {
        gameService = GameService(mockk())
    }

    @Test
    fun `should retrieve riddle content`() {
        assertThat(gameService.retrieveRiddleContent("riddle-1"))
            .isEqualTo("# First riddle")
    }

    @ParameterizedTest
    @CsvSource(
        "riddle-1, solution-1, true",
        "riddle-1, solution-2, false",
        "unknown, solution-2, false")
    fun `should check riddle solution`(riddleName: String, solution: String, expectedResult: Boolean) {
        assertThat(gameService.isCorrect(riddleName, solution))
            .isEqualTo(expectedResult)
    }
}