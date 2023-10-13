package me.elgregos.escapecamp.game.domain.event

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.every
import io.mockk.mockk
import me.elgregos.escapecamp.config.exception.GameException
import me.elgregos.escapecamp.game.domain.entity.*
import me.elgregos.escapecamp.game.domain.service.MockedRiddleSolutionChecker
import me.elgregos.reakteves.domain.event.EventStore
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.LocalDateTime
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class GameAggregateEnrollContestantTest {

    private lateinit var gameEventStore: EventStore<GameEvent, UUID, UUID>

    @BeforeTest
    fun setup() {
        gameEventStore = mockk()
    }

    @Test
    fun `should enroll first contestant to game`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.just(escapeCampCreated)

        GameAggregate(escapeCampId, lockedAndLoadedContestantId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .enrollContestant(lockedAndLoadedContestant, lockedAndLoadedContestantEnrolledAt)
            .`as`(StepVerifier::create)
            .assertNext { assertThat(it).isEqualTo(lockedAndLoadedContestantEnrolled.copy(id = it.id)) }
            .verifyComplete()
    }

    @Test
    fun `should enroll last contestant to game and start game`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.just(
            escapeCampCreated,
            lockedAndLoadedContestantEnrolled
        )

        GameAggregate(escapeCampId, jeepersKeypersContestantId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .enrollContestant(jeepersKeypersContestant, jeepersKeypersContestantEnrolledAt)
            .`as`(StepVerifier::create)
            .assertNext { assertThat(it).isEqualTo(jeepersKeypersContestantEnrolled.copy(id = it.id)) }
            .assertNext { assertThat(it).isEqualTo(escapeCampStarted.copy(id = it.id)) }
            .verifyComplete()
    }

    @Test
    fun `should fail to enroll contestant to game when 2 contestants exist`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.just(
            escapeCampCreated,
            lockedAndLoadedContestantEnrolled,
            jeepersKeypersContestantEnrolled
        )
        val unexpectedContestantId = UUID.randomUUID()

        GameAggregate(escapeCampId, unexpectedContestantId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .enrollContestant(Contestant(unexpectedContestantId, "unexpectedContestant"), LocalDateTime.now())
            .`as`(StepVerifier::create)
            .verifyErrorMatches { throwable -> throwable is GameException.ContestantNumberLimitExceededException }
    }

    @Test
    fun `should fail to enroll contestant to game with same name`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.just(
            escapeCampCreated,
            lockedAndLoadedContestantEnrolled
        )

        GameAggregate(escapeCampId, lockedAndLoadedContestantId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .enrollContestant(lockedAndLoadedContestant, LocalDateTime.now())
            .`as`(StepVerifier::create)
            .verifyErrorMatches { throwable ->
                throwable is GameException.ContestantNameNotAvailableException || throwable.message.equals(
                    "Contestant with name Lock and loaded already exists"
                )
            }
    }

    @Test
    fun `should fail to enroll contestant to non-existent game`() {
        every { gameEventStore.loadAllEvents(unknownGameId) } returns Flux.empty()

        GameAggregate(unknownGameId, lockedAndLoadedContestantId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .enrollContestant(lockedAndLoadedContestant, lockedAndLoadedContestantEnrolledAt)
            .`as`(StepVerifier::create)
            .verifyErrorMatches { throwable ->
                throwable is GameException.GameNotFoundException || throwable.message.equals(
                    "Game with id 07c905e7-8179-4b59-a65a-510a4e1de4d3 has not been found"
                )
            }
    }
}
