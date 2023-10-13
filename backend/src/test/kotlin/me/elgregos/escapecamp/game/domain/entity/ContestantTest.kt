package me.elgregos.escapecamp.game.domain.entity

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Named.named
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.Test

class ContestantTest {

    @Test
    fun `should assign first riddle to contestant`() {
        assertThat(lockedAndLoadedContestant.assignRiddle(lockedAndLoadedFirstRiddle)).isEqualTo(
            lockedAndLoadedContestantAfterFirstRiddleAssigned)
    }

    @ParameterizedTest
    @MethodSource("hasPreviousRiddleSolvedTestCases")
    fun `should check if previous riddle is solved`(contestant: Contestant, expectedResult: Boolean) {
        assertThat(contestant.hasPreviousRiddleSolved()).isEqualTo(expectedResult)
    }

    @Test
    fun `should find last unsolved riddle`() {
        assertThat(lockedAndLoadedContestantAfterFirstRiddleAssigned.lastUnsolvedRiddle())
            .isEqualTo(lockedAndLoadedFirstRiddle)
    }

    @Test
    fun `should solve last unsolved riddle`() {
        assertThat(jeepersKeypersContestantAfterFirstRiddleAssigned.solveLastUnsolvedRiddle(jeepersKeypersFirstRiddleSolvedAt))
            .isEqualTo(jeepersKeypersContestantAfterFirstRiddleSolved)
    }

    @Test
    fun `should get number of solved riddle`() {
        assertThat(jeepersKeypersContestantAfterFirstRiddleSolved.numberOfSolvedRiddles())
            .isEqualTo(1)
    }

    @ParameterizedTest
    @MethodSource("hasSolvedAllRiddlesTestCases")
    fun `should check if contestant has solved all riddles`(contestant: Contestant, expectedResult: Boolean) {
        assertThat(contestant.hasSolvedAllRiddles(riddles)).isEqualTo(expectedResult)
    }

    companion object {
        @JvmStatic
        fun hasPreviousRiddleSolvedTestCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(named("No previous riddle assigned", lockedAndLoadedContestant), true),
                Arguments.of(named("Previous riddle not solved", lockedAndLoadedContestantAfterFirstRiddleAssigned), false),
                Arguments.of(named("Previous riddle solved", lockedAndLoadedContestantAfterFirstRiddleSolved), true)
            )

        @JvmStatic
        fun hasSolvedAllRiddlesTestCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(named("All riddles not solved", jeepersKeypersContestantAfterFirstRiddleSolved), false),
                Arguments.of(named("All riddles  solved", jeepersKeypersContestantAfterSecondRiddleSolved), true)
            )
    }
}
