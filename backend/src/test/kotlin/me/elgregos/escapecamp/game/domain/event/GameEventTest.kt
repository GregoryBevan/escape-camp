package me.elgregos.escapecamp.game.domain.event

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import kotlin.test.Test

class GameEventTest {

    @Test
    fun `should get current riddle`() {
        assertThat(firstRiddleUnlocked.currentRiddle()).isEqualTo(0)
    }

    @Test
    fun `should obtain last contestant riddle`() {
        assertThat(lockedAndLoadedFirstRiddleAssigned.assignedRiddle()).isNotNull()

    }
}
