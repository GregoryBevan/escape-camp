package me.elgregos.escapecamp.game.domain.entity

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlin.test.Test

class RiddleTest {

    @Test
    fun `should return not solved when riddle solved at is null`() {
        assertThat(lockedAndLoadedFirstRiddle.hasBeenSolved()).isFalse()
    }

    @Test
    fun `should return solved when riddle solved at is set`() {
        assertThat(lockedAndLoadedFirstSolvedRiddle.hasBeenSolved()).isTrue()
    }

    @Test
    fun `should solve riddle`() {
        assertThat(jeepersKeypersFirstRiddle.solved(jeepersKeypersFirstRiddleSolvedAt))
            .isEqualTo(jeepersKeypersFirstSolvedRiddle)
    }
}