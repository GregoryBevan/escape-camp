package me.elgregos.escapecamp.game.domain.entity

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Named
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.Duration
import java.util.stream.Stream

internal class LeaderBoardTest {

    @ParameterizedTest
    @MethodSource("gameTestCases")
    fun `should calculate leaderboard`(game: Game, lines: List<Line>) {
        assertThat(LeaderBoard.ofGame(game)).isEqualTo(LeaderBoard(game.id, lines))
    }

    companion object {
        @JvmStatic
        fun gameTestCases(): Stream<Arguments> = Stream.of(
            Arguments.of(Named.named("No contestant enrolled", escapeCamp), listOf<Line>()),
            Arguments.of(
                Named.named("Lock and Loaded contestant enrolled", escapeCampAfterLockedAndLoadedContestantEnrolled),
                listOf(Line(lockedAndLoadedContestantName, 0, Duration.ZERO))
            ),
            Arguments.of(
                Named.named("Jeepers Keypers contestant enrolled", escapeCampAfterJeepersKeypersContestantEnrolled),
                listOf(
                    Line(lockedAndLoadedContestantName, 0, Duration.ZERO),
                    Line(jeepersKeypersContestantName, 0, Duration.ZERO)
                )
            ),
            Arguments.of(
                Named.named("After Jeepers Keypers's first riddle solved", escapeCampAfterJeepersKeypersFirstRiddleSolved),
                listOf(
                    Line(jeepersKeypersContestantName, 1, Duration.ofSeconds(281)),
                    Line(lockedAndLoadedContestantName, 0, Duration.ZERO)
                )
            ),
            Arguments.of(
                Named.named("After Locked and Loaded's first riddle solved", unlimitedEnrollmentEscapeCampAfterLockedAndLoadedFirstRiddleSolved),
                listOf(
                    Line(jeepersKeypersContestantName, 1, Duration.ofSeconds(281)),
                    Line(lockedAndLoadedContestantName, 1, Duration.ofSeconds(464))
                )
            ),
            Arguments.of(
                Named.named("After Jeepers Keypers's second riddle solved", escapeCampAfterJeepersKeypersSecondRiddleSolved),
                listOf(
                    Line(jeepersKeypersContestantName, 2, Duration.ofSeconds(589)),
                    Line(lockedAndLoadedContestantName, 1, Duration.ofSeconds(464))
                )
            ),
            Arguments.of(
                Named.named("After Locked and Loaded's second riddle solved", escapeCampAfterLockAndLoadedSecondRiddleSolved),
                listOf(
                    Line(jeepersKeypersContestantName, 2, Duration.ofSeconds(589)),
                    Line(lockedAndLoadedContestantName, 2, Duration.ofSeconds(827))
                )
            )
        )
    }
}
