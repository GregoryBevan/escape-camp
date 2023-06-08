package me.elgregos.escapecamp.game.application

import assertk.assertThat
import assertk.assertions.isEqualTo
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
}