package me.elgregos.escapecamp.game.domain.entity

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Named
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Stream
import kotlin.test.Test

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
    @ParameterizedTest
    @MethodSource("checkIfTeamExistsTestCases")
    fun `should check if team exist`(teamId: UUID, expectedResult: Boolean) {
        assertThat(escapeCampAfterGameStarted.checkIfTeamExists(teamId)).isEqualTo(expectedResult)
    }

    @ParameterizedTest
    @MethodSource("canAssignRiddleToTeamTestCases")
    fun `should check if riddle is assignable to team`(game: Game, expectedResult: Boolean) {
        assertThat(game.canAssignRiddleToTeam(lockedAndLoadedTeamId))
            .isEqualTo(expectedResult)
    }

    @ParameterizedTest
    @MethodSource("assignRiddleTeamTestCases")
    fun `should assign riddle to team`(game: Game, teamId: UUID, assignedAt: LocalDateTime, expectedGame: Game) {
        assertThat(game.assignRiddleToTeam(teamId, assignedAt)).isEqualTo(expectedGame)
    }

    @Test
    fun `should find last unsolved riddle for team`() {
        assertThat(escapeCampAfterAllFirstRiddleAssigned.teamLastUnsolvedRiddle(jeepersKeypersTeamId))
            .isEqualTo(jeepersKeypersFirstRiddle)
    }

    @Test
    fun `should solve last assigned riddle of team`() {
        assertThat(escapeCampAfterAllFirstRiddleAssigned.solveLastAssignedRiddleOfTeam(jeepersKeypersTeamId, jeepersKeypersFirstRiddleSolvedAt))
            .isEqualTo(escapeCampAfterJeepersKeypersFirstRiddleSolved)
    }

    companion object {

        @JvmStatic
        fun checkIfTeamExistsTestCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(lockedAndLoadedTeamId, true),
                Arguments.of(unknownTeamId, false),
            )

        @JvmStatic
        fun canAssignRiddleToTeamTestCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(Named.named("No previous riddle assigned to the team", escapeCampAfterGameStarted), true),
                Arguments.of(Named.named("After riddle assigned to the team", escapeCampAfterLockedAndLoadedFirstRiddleAssigned), false),
                Arguments.of(Named.named("After riddle solved by team", escapeCampAfterLockedAndLoadedFirstRiddleSolved), true),
            )

        @JvmStatic
        fun assignRiddleTeamTestCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(Named.named("First registered team - First riddle", escapeCampAfterGameStarted), lockedAndLoadedTeamId, lockedAndLoadedFirstRiddleAssignedAt, escapeCampAfterLockedAndLoadedFirstRiddleAssigned),
                Arguments.of(Named.named("Fourth registered team - First riddle", escapeCampAfterLockedAndLoadedFirstRiddleAssigned), sherUnlockTeamId, sherUnlockFirstRiddleAssignedAt, escapeCampAfterSherUnlockFirstRiddleAssigned),
                Arguments.of(Named.named("Third registered team - First riddle", escapeCampAfterSherUnlockFirstRiddleAssigned), theEscapePeasTeamId, theEscapePeasFirstRiddleAssignedAt, escapeCampAfterTheEscapePeasFirstRiddleAssigned),
                Arguments.of(Named.named("Second registered team - First riddle", escapeCampAfterTheEscapePeasFirstRiddleAssigned), jeepersKeypersTeamId, jeepersKeypersFirstRiddleAssignedAt, escapeCampAfterAllFirstRiddleAssigned),
                Arguments.of(Named.named("Second registered team - Second riddle", escapeCampAfterJeepersKeypersFirstRiddleSolved), jeepersKeypersTeamId, jeepersKeypersSecondRiddleAssignedAt, escapeCampAfterJeepersKeypersSecondRiddleAssigned),
            )
    }

}