package me.elgregos.escapecamp.game.domain.entity

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.escapecamp.game.domain.event.lockedAndLoadedTeamAddedAt
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.util.*

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

    @ParameterizedTest
    @CsvSource(
        "3a66dce7-ca96-4cd0-af56-6bd7e082edd5, 1",
        "a6e05314-2af2-43c7-a274-d024cf053b42, 3",
        "0bfce65c-dff9-4f2e-8e8f-11ed6151b205, 0",
        "91700c93-10f9-474e-8176-811598a9aaef, 2"
    )
    fun `should get team registration order`(teamId: UUID, expectedOrder: Int) {
        assertThat(escapeCampAfterGameStarted.teamRegistrationOrder(teamId))
            .isEqualTo(expectedOrder)
    }

    @Test
    fun `should assign riddle to team`() {
        assertThat(escapeCampAfterGameStarted.assignRiddleToTeam(lockedAndLoadedTeamId, lockedAndLoadedFirstRiddle))
            .isEqualTo(escapeCampAfterLockedAndLoadedFirstRiddleAssigned)
    }

}