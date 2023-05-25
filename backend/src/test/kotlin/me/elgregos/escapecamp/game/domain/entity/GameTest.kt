package me.elgregos.escapecamp.game.domain.entity

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class GameTest {

    @Test
    fun `should add first team to game`() {
        assertThat(escapeCamp.addTeam(lockedAndLoadedTeam)).isEqualTo(
            escapeCampAfterLockedAndLoadedTeamAdded
        )
    }

}