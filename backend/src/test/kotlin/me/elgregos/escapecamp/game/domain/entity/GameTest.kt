package me.elgregos.escapecamp.game.domain.entity

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.escapecamp.game.domain.event.lockedAndLoadedTeamAddedAt
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GameTest {

    @Test
    fun `should add team to game`() {
        assertThat(escapeCamp.addTeam(lockedAndLoadedTeam, lockedAndLoadedTeamAddedAt))
            .isEqualTo(escapeCampAfterLockedAndLoadedTeamAdded)
    }

    @ParameterizedTest
    @CsvSource(
        "Locked and Loaded, false",
        "Jeepers Keypers, true"
    )
    fun `should check if team name is available`(teamName: String, expectedResult: Boolean) {
        assertThat(escapeCampAfterLockedAndLoadedTeamAdded.isTeamNameAvailable(teamName)).isEqualTo(expectedResult)
    }

    @Test
    fun `should assign first riddle to team`() {
        assertThat(escapeCampAfterGameStarted.assignRiddleToTeam(lockedAndLoadedTeamId, lockedAndLoadedFirstRiddle))
            .isEqualTo(escapeCampAfterLockedAndLoadedFirstRiddleAssigned)
    }

}