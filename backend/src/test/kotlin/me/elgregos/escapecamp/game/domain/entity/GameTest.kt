package me.elgregos.escapecamp.game.domain.entity

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Named.*
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
        "0bfce65c-dff9-4f2e-8e8f-11ed6151b205, 0"
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

    @ParameterizedTest
    @MethodSource("isFirstTeamToSolveAllRiddleTestCases")
    fun `should check if game has a winner`(game: Game, expectedResult: Boolean) {
        assertThat(game.checkIfIsFirstTeamToSolveAllRiddle()).isEqualTo(expectedResult)
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
                Arguments.of(named("No previous riddle assigned to the team", escapeCampAfterGameStarted), true),
                Arguments.of(named("After riddle assigned to the team", escapeCampAfterLockedAndLoadedFirstRiddleAssigned), false),
                Arguments.of(named("After riddle solved by team", escapeCampAfterLockedAndLoadedFirstRiddleSolved), true),
            )

        @JvmStatic
        fun assignRiddleTeamTestCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(named("First registered team - First riddle", escapeCampAfterGameStarted), lockedAndLoadedTeamId, lockedAndLoadedFirstRiddleAssignedAt, escapeCampAfterLockedAndLoadedFirstRiddleAssigned),
                Arguments.of(named("Second registered team - First riddle", escapeCampAfterLockedAndLoadedFirstRiddleAssigned), jeepersKeypersTeamId, jeepersKeypersFirstRiddleAssignedAt, escapeCampAfterAllFirstRiddleAssigned),
                Arguments.of(named("Second registered team - Second riddle", escapeCampAfterJeepersKeypersFirstRiddleSolved), jeepersKeypersTeamId, jeepersKeypersSecondRiddleAssignedAt, escapeCampAfterJeepersKeypersSecondRiddleAssigned),
                Arguments.of(named("First registered team - Second riddle", escapeCampAfterLockedAndLoadedFirstRiddleSolved), lockedAndLoadedTeamId, lockedAndLoadedSecondRiddleAssignedAt, escapeCampAfterLockedAndLoadedSecondRiddleAssigned),
            )

        @JvmStatic
        fun isFirstTeamToSolveAllRiddleTestCases(): Stream<Arguments> =
            Stream.of(
//                Arguments.of(named("No team has solved all riddles", escapeCampAfterLockedAndLoadedSecondRiddleAssigned), false),
                Arguments.of(named("First team to solve all riddle", escapeCampAfterJeepersKeypersSecondRiddleSolved), true),
//                Arguments.of(named("Second team to solve all riddle", escapeCampAfterLockAndLoadedSecondRiddleSolved), false)
            )
    }

}