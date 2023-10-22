package me.elgregos.escapecamp.game.domain.entity

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Named.named
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Stream
import kotlin.test.Test

internal class GameTest {

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
        assertThat(escapeCampAfterLockedAndLoadedContestantEnrolled.contestantNameAvailable(contestantName)).isEqualTo(
            expectedResult
        )
    }

    @ParameterizedTest
    @MethodSource("checkContestantLimitTestCases")
    fun `should check if contestant can be enrolled according to limitation`(game: Game, expectedResult: Boolean) {
        assertThat(game.contestantLimitNotReached()).isEqualTo(expectedResult)
    }

    @ParameterizedTest
    @MethodSource("checkIfAbleToStartAutomaticallyTestCases")
    fun `should check if game is able to start automatically`(game: Game, expectedResult: Boolean) {
        assertThat(game.ableToStartAutomatically()).isEqualTo(expectedResult)
    }

    @ParameterizedTest
    @MethodSource("unlockRiddleAllowedTestCases")
    fun `should check if a riddle can be unlocked`(game: Game, expectedResult: Boolean) {
        assertThat(game.ableToUnlockNextRiddle()).isEqualTo(expectedResult)
    }

    @ParameterizedTest
    @MethodSource("checkIfGameIsStartedTestCases")
    fun `should check if game is started`(game: Game, expectedResult: Boolean) {
        assertThat(game.started()).isEqualTo(expectedResult)
    }

    @ParameterizedTest
    @MethodSource("nextRiddleTestCases")
    fun `should get next riddle`(game: Game, expectedRiddle: Int) {
        assertThat(game.nextRiddleToUnlock()).isEqualTo(expectedRiddle)
    }

    @ParameterizedTest
    @CsvSource(
        "3a66dce7-ca96-4cd0-af56-6bd7e082edd5, 1",
        "0bfce65c-dff9-4f2e-8e8f-11ed6151b205, 0"
    )
    fun `should get contestant enrollment order`(contestantId: UUID, expectedOrder: Int) {
        assertThat(unlimitedEnrollmentEscapeCampAfterGameStarted.contestantEnrollmentOrder(contestantId))
            .isEqualTo(expectedOrder)
    }

    @ParameterizedTest
    @MethodSource("checkIfContestantExistsTestCases")
    fun `should check if contestant exist`(contestantId: UUID, expectedResult: Boolean) {
        assertThat(unlimitedEnrollmentEscapeCampAfterGameStarted.checkIfContestantExists(contestantId)).isEqualTo(expectedResult)
    }

    @ParameterizedTest
    @MethodSource("hasSolvedAllUnlockedRiddlesTestCases")
    fun `should check if contestant has solved previous riddles in game with unlimited enrollment`(game: Game, expectedResult: Boolean) {
        assertThat(game.hasSolvedAllUnlockedRiddles(lockedAndLoadedContestantId))
            .isEqualTo(expectedResult)
    }

    @ParameterizedTest
    @MethodSource("canAssignRiddleToContestantTestCases")
    fun `should check if riddle is assignable to contestant`(game: Game, expectedResult: Boolean) {
        assertThat(game.canAssignRiddleToContestant(lockedAndLoadedContestantId))
            .isEqualTo(expectedResult)
    }

    @ParameterizedTest
    @MethodSource("assignRiddleContestantTestCases")
    fun `should assign riddle to contestant`(
        game: Game,
        contestantId: UUID,
        assignedAt: LocalDateTime,
        expectedGame: Game
    ) {
        assertThat(game.assignRiddleToContestant(contestantId, assignedAt)).isEqualTo(expectedGame)
    }

    @Test
    fun `should find last unsolved riddle for contestant`() {
        assertThat(escapeCampAfterAllFirstRiddleAssigned.contestantLastUnsolvedRiddle(jeepersKeypersContestantId))
            .isEqualTo(jeepersKeypersFirstRiddle)
    }

    @Test
    fun `should solve last assigned riddle of contestant`() {
        assertThat(
            escapeCampAfterAllFirstRiddleAssigned.solveLastAssignedRiddleOfContestant(
                jeepersKeypersContestantId,
                jeepersKeypersFirstRiddleSolvedAt
            )
        ).isEqualTo(escapeCampAfterJeepersKeypersFirstRiddleSolved)
    }

    @ParameterizedTest
    @MethodSource("isFirstContestantToSolveAllRiddleTestCases")
    fun `should check if game has a winner`(game: Game, expectedResult: Boolean) {
        assertThat(game.checkIfIsFirstContestantToSolveAllRiddle()).isEqualTo(expectedResult)
    }

    companion object {

        @JvmStatic
        fun checkContestantLimitTestCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(named("Game created", escapeCamp), true),
                Arguments.of(named("Game created without contestant limit", unlimitedEnrollmentEscapeCamp), true),
                Arguments.of(named("Game with one contestant", escapeCampAfterLockedAndLoadedContestantEnrolled), true),
                Arguments.of(
                    named("Game with two contestants", escapeCampAfterJeepersKeypersContestantEnrolled),
                    false
                ),
                Arguments.of(
                    named(
                        "Game with two contestant without contestant limit",
                        unlimitedEnrollmentEscapeCampAfterJeepersKeypersContestantEnrolled
                    ), true
                )
            )

        @JvmStatic
        fun checkIfAbleToStartAutomaticallyTestCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(named("Game created", escapeCamp), false),
                Arguments.of(named("Game created without contestant limit", unlimitedEnrollmentEscapeCamp), false),
                Arguments.of(
                    named("Game with one contestant", escapeCampAfterLockedAndLoadedContestantEnrolled),
                    false
                ),
                Arguments.of(named("Game with two contestants", escapeCampAfterJeepersKeypersContestantEnrolled), true),
                Arguments.of(
                    named(
                        "Game with two contestant without contestant limit",
                        unlimitedEnrollmentEscapeCampAfterJeepersKeypersContestantEnrolled
                    ), false
                )
            )

        @JvmStatic
        fun checkIfContestantExistsTestCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(lockedAndLoadedContestantId, true),
                Arguments.of(unknownContestantId, false),
            )

        @JvmStatic
        fun nextRiddleTestCases(): Stream<Arguments> = Stream.of(
            Arguments.of(named("After game created", escapeCamp), 0),
            Arguments.of(named("After first riddle unlocked", escapeCampWithFirstRiddleUnlocked), 1)
        )

        @JvmStatic
        fun unlockRiddleAllowedTestCases(): Stream<Arguments> = Stream.of(
            Arguments.of(named("After game created", escapeCamp), false),
            Arguments.of(named("After unlimited enrollment game created", unlimitedEnrollmentEscapeCamp), true),
            Arguments.of(named("After first riddle unlocked", escapeCampWithFirstRiddleUnlocked), true),
            Arguments.of(named("After second riddle unlocked", unlimitedEscapeCampWithSecondRiddleUnlocked), false)
        )

        @JvmStatic
        fun checkIfGameIsStartedTestCases(): Stream<Arguments> = Stream.of(
            Arguments.of(named("After game created", escapeCamp), false),
            Arguments.of(named("After unlimited enrollment game created", unlimitedEnrollmentEscapeCamp), false),
            Arguments.of(named("After first riddle unlocked", escapeCampWithFirstRiddleUnlocked), true),
            Arguments.of(named("After second riddle unlocked", unlimitedEscapeCampWithSecondRiddleUnlocked), true)
        )

        @JvmStatic
        fun hasSolvedAllUnlockedRiddlesTestCases(): Stream<Arguments> = Stream.of(
            Arguments.of(named("Limited : No previous riddle assigned to the contestant", escapeCampAfterGameStarted), true),
            Arguments.of(named("Limited : After riddle assigned to the contestant", escapeCampAfterLockedAndLoadedFirstRiddleAssigned), true),
            Arguments.of(named("Limited : After riddle solved by contestant", escapeCampAfterLockedAndLoadedFirstRiddleSolved), true),
            Arguments.of(named("Unlimited : No riddle unlocked", unlimitedEnrollmentEscapeCampAfterJeepersKeypersContestantEnrolled), false),
            Arguments.of(named("Unlimited : No previous riddle assigned to the contestant", escapeCampWithFirstRiddleUnlocked), true),
            Arguments.of(named("Unlimited : After first riddle assigned to contestant", unlimitedEnrollmentEscapeCampAfterLockedAndLoadedFirstRiddleAssigned), true),
            Arguments.of(named("Unlimited : First riddle solved by contestant", unlimitedEnrollmentEscapeCampAfterLockedAndLoadedFirstRiddleSolved), false),
            Arguments.of(named("Unlimited : After second riddle unlocked", unlimitedEscapeCampWithSecondRiddleUnlocked), true),
        )

        @JvmStatic
        fun canAssignRiddleToContestantTestCases(): Stream<Arguments> = Stream.of(
            Arguments.of(named("Limited : No previous riddle assigned to the contestant", escapeCampAfterGameStarted), true),
            Arguments.of(named("Limited : After riddle assigned to the contestant", escapeCampAfterLockedAndLoadedFirstRiddleAssigned), false),
            Arguments.of(named("Limited : After riddle solved by contestant", escapeCampAfterLockedAndLoadedFirstRiddleSolved), true),
            Arguments.of(named("Unlimited : No riddle unlocked", unlimitedEnrollmentEscapeCampAfterJeepersKeypersContestantEnrolled), false),
            Arguments.of(named("Unlimited : No previous riddle assigned to the contestant", escapeCampWithFirstRiddleUnlocked), true),
            Arguments.of(named("Unlimited : After first riddle assigned to contestant", unlimitedEnrollmentEscapeCampAfterLockedAndLoadedFirstRiddleAssigned), false),
            Arguments.of(named("Unlimited : First riddle solved by contestant", unlimitedEnrollmentEscapeCampAfterLockedAndLoadedFirstRiddleSolved), false),
            Arguments.of(named("Unlimited : After second riddle unlocked", unlimitedEscapeCampWithSecondRiddleUnlocked), true),
        )

        @JvmStatic
        fun assignRiddleContestantTestCases(): Stream<Arguments> = Stream.of(
            Arguments.of(
                named("First registered contestant - First riddle", unlimitedEnrollmentEscapeCampAfterGameStarted),
                lockedAndLoadedContestantId,
                lockedAndLoadedFirstRiddleAssignedAt,
                unlimitedEnrollmentEscapeCampAfterLockedAndLoadedFirstRiddleAssigned
            ),
            Arguments.of(
                named(
                    "Second registered contestant - First riddle", escapeCampAfterLockedAndLoadedFirstRiddleAssigned
                ),
                jeepersKeypersContestantId,
                jeepersKeypersFirstRiddleAssignedAt,
                escapeCampAfterAllFirstRiddleAssigned
            ),
            Arguments.of(
                named(
                    "Unlimited - Second registered contestant - First riddle", escapeCampAfterLockedAndLoadedFirstRiddleAssigned
                ),
                jeepersKeypersContestantId,
                jeepersKeypersFirstRiddleAssignedAt,
                escapeCampAfterAllFirstRiddleAssigned
            ),
            Arguments.of(
                named(
                    "Second registered contestant - Second riddle", escapeCampAfterJeepersKeypersFirstRiddleSolved
                ),
                jeepersKeypersContestantId,
                jeepersKeypersSecondRiddleAssignedAt,
                escapeCampAfterJeepersKeypersSecondRiddleAssigned
            ),
            Arguments.of(
                named(
                    "First registered contestant - Second riddle", escapeCampAfterLockedAndLoadedFirstRiddleSolved
                ),
                lockedAndLoadedContestantId,
                lockedAndLoadedSecondRiddleAssignedAt,
                escapeCampAfterLockedAndLoadedSecondRiddleAssigned
            ),
        )

        @JvmStatic
        fun isFirstContestantToSolveAllRiddleTestCases(): Stream<Arguments> =
            Stream.of(
//                Arguments.of(named("No contestant has solved all riddles", escapeCampAfterLockedAndLoadedSecondRiddleAssigned), false),
                Arguments.of(
                    named(
                        "First contestant to solve all riddle",
                        escapeCampAfterJeepersKeypersSecondRiddleSolved
                    ), true
                ),
//                Arguments.of(named("Second contestant to solve all riddle", escapeCampAfterLockAndLoadedSecondRiddleSolved), false)
            )
    }

}
