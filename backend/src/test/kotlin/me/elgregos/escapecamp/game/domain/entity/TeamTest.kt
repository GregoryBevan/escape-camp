package me.elgregos.escapecamp.game.domain.entity

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class TeamTest {

    @Test
    fun `should assign first riddle to team`() {
        assertThat(lockedAndLoadedTeam.assignRiddle(lockedAndLoadedFirstRiddle)).isEqualTo(
            lockedAndLoadedTeamAfterFirstRiddleAssigned)
    }
}