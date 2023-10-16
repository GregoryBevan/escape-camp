package me.elgregos.escapecamp.game.application

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.every
import io.mockk.mockk
import me.elgregos.escapecamp.game.domain.entity.*
import me.elgregos.reakteves.domain.projection.ProjectionStore
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Duration
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class GameServiceTest {

    private lateinit var gameService: GameService

    private lateinit var projectionStore: ProjectionStore<Game, UUID, UUID>

    @BeforeTest
    fun setUp() {
        projectionStore = mockk()
        gameService = GameService(projectionStore, riddles)
    }

    @Test
    fun `should get game leaderboard`() {
        every { projectionStore.find(escapeCampId) }.returns(Mono.just(escapeCampAfterJeepersKeypersSecondRiddleSolved))

        gameService.gameLeaderBoard(escapeCampId)
            .`as`(StepVerifier::create)
            .assertNext {
                assertThat(it).isEqualTo(
                    LeaderBoard(
                        escapeCampId,
                        listOf(
                            Line(jeepersKeypersContestantName, 2, Duration.ofSeconds(589)),
                            Line(lockedAndLoadedContestantName, 1, Duration.ofSeconds(464))
                        )
                    )
                )
            }
            .verifyComplete()
    }
}
