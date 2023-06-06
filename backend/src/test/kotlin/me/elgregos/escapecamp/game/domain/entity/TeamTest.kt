package me.elgregos.escapecamp.game.domain.entity

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Named.named
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.Test

class TeamTest {

    @Test
    fun `should assign first riddle to team`() {
        assertThat(lockedAndLoadedTeam.assignRiddle(lockedAndLoadedFirstRiddle)).isEqualTo(
            lockedAndLoadedTeamAfterFirstRiddleAssigned)
    }


    @ParameterizedTest
    @MethodSource("previousRiddleSolvedTestCases")
    fun `should check if previous riddle is solved`(team: Team, expectedResult: Boolean) {
        assertThat(team.previousRiddleSolved()).isEqualTo(expectedResult)
    }

    companion object {
        @JvmStatic
        fun previousRiddleSolvedTestCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(named("No previous riddle assigned", lockedAndLoadedTeam), true),
                Arguments.of(named("Previous riddle not solved", lockedAndLoadedTeamAfterFirstRiddleAssigned), false),
                Arguments.of(named("Previous riddle solved", lockedAndLoadedTeamAfterFirstRiddleSolved), true)
            )
    }
}