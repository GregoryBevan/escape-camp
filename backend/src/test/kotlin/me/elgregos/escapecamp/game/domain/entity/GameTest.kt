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
    fun `should enroll contestant to game`() {
        assertThat(escapeCamp.enrollContestant(lockedAndLoadedContestant, lockedAndLoadedContestantEnrolledAt))
            .isEqualTo(escapeCampAfterLockedAndLoadedContestantEnrolled)
    }

    @ParameterizedTest
    @CsvSource(
        "Locked and Loaded, false",
        "Jeepers Keypers, true"
    )
    fun `should check if contestant name is available`(contestantName: String, expectedResult: Boolean) {
        assertThat(escapeCampAfterLockedAndLoadedContestantEnrolled.isContestantNameAvailable(contestantName)).isEqualTo(expectedResult)
    }

    @ParameterizedTest
    @CsvSource(
        "3a66dce7-ca96-4cd0-af56-6bd7e082edd5, 1",
        "0bfce65c-dff9-4f2e-8e8f-11ed6151b205, 0"
    )
    fun `should get contestant registration order`(contestantId: UUID, expectedOrder: Int) {
        assertThat(escapeCampAfterGameStarted.contestantRegistrationOrder(contestantId))
            .isEqualTo(expectedOrder)
    }
    @ParameterizedTest
    @MethodSource("checkIfContestantExistsTestCases")
    fun `should check if contestant exist`(contestantId: UUID, expectedResult: Boolean) {
        assertThat(escapeCampAfterGameStarted.checkIfContestantExists(contestantId)).isEqualTo(expectedResult)
    }

    @ParameterizedTest
    @MethodSource("canAssignRiddleToContestantTestCases")
    fun `should check if riddle is assignable to contestant`(game: Game, expectedResult: Boolean) {
        assertThat(game.canAssignRiddleToContestant(lockedAndLoadedContestantId))
            .isEqualTo(expectedResult)
    }

    @ParameterizedTest
    @MethodSource("assignRiddleContestantTestCases")
    fun `should assign riddle to contestant`(game: Game, contestantId: UUID, assignedAt: LocalDateTime, expectedGame: Game) {
        assertThat(game.assignRiddleToContestant(contestantId, assignedAt)).isEqualTo(expectedGame)
    }

    @Test
    fun `should find last unsolved riddle for contestant`() {
        assertThat(escapeCampAfterAllFirstRiddleAssigned.contestantLastUnsolvedRiddle(jeepersKeypersContestantId))
            .isEqualTo(jeepersKeypersFirstRiddle)
    }

    @Test
    fun `should solve last assigned riddle of contestant`() {
        assertThat(escapeCampAfterAllFirstRiddleAssigned.solveLastAssignedRiddleOfContestant(jeepersKeypersContestantId, jeepersKeypersFirstRiddleSolvedAt))
            .isEqualTo(escapeCampAfterJeepersKeypersFirstRiddleSolved)
    }

    @ParameterizedTest
    @MethodSource("isFirstContestantToSolveAllRiddleTestCases")
    fun `should check if game has a winner`(game: Game, expectedResult: Boolean) {
        assertThat(game.checkIfIsFirstContestantToSolveAllRiddle()).isEqualTo(expectedResult)
    }

    companion object {

        @JvmStatic
        fun checkIfContestantExistsTestCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(lockedAndLoadedContestantId, true),
                Arguments.of(unknownContestantId, false),
            )

        @JvmStatic
        fun canAssignRiddleToContestantTestCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(named("No previous riddle assigned to the contestant", escapeCampAfterGameStarted), true),
                Arguments.of(named("After riddle assigned to the contestant", escapeCampAfterLockedAndLoadedFirstRiddleAssigned), false),
                Arguments.of(named("After riddle solved by contestant", escapeCampAfterLockedAndLoadedFirstRiddleSolved), true),
            )

        @JvmStatic
        fun assignRiddleContestantTestCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(named("First registered contestant - First riddle", escapeCampAfterGameStarted), lockedAndLoadedContestantId, lockedAndLoadedFirstRiddleAssignedAt, escapeCampAfterLockedAndLoadedFirstRiddleAssigned),
                Arguments.of(named("Second registered contestant - First riddle", escapeCampAfterLockedAndLoadedFirstRiddleAssigned), jeepersKeypersContestantId, jeepersKeypersFirstRiddleAssignedAt, escapeCampAfterAllFirstRiddleAssigned),
                Arguments.of(named("Second registered contestant - Second riddle", escapeCampAfterJeepersKeypersFirstRiddleSolved), jeepersKeypersContestantId, jeepersKeypersSecondRiddleAssignedAt, escapeCampAfterJeepersKeypersSecondRiddleAssigned),
                Arguments.of(named("First registered contestant - Second riddle", escapeCampAfterLockedAndLoadedFirstRiddleSolved), lockedAndLoadedContestantId, lockedAndLoadedSecondRiddleAssignedAt, escapeCampAfterLockedAndLoadedSecondRiddleAssigned),
            )

        @JvmStatic
        fun isFirstContestantToSolveAllRiddleTestCases(): Stream<Arguments> =
            Stream.of(
//                Arguments.of(named("No contestant has solved all riddles", escapeCampAfterLockedAndLoadedSecondRiddleAssigned), false),
                Arguments.of(named("First contestant to solve all riddle", escapeCampAfterJeepersKeypersSecondRiddleSolved), true),
//                Arguments.of(named("Second contestant to solve all riddle", escapeCampAfterLockAndLoadedSecondRiddleSolved), false)
            )
    }

}
