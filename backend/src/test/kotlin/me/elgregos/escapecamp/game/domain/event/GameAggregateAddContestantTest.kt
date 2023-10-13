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

class GameAggregateAddContestantTest {

    private lateinit var gameEventStore: EventStore<GameEvent, UUID, UUID>

    @BeforeTest
    fun setup() {
        gameEventStore = mockk()
    }

    @Test
    fun `should add first contestant to game`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.just(escapeCampCreated)

        GameAggregate(escapeCampId, lockedAndLoadedContestantId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .addContestant(lockedAndLoadedContestant, lockedAndLoadedContestantAddedAt)
            .`as`(StepVerifier::create)
            .assertNext { assertThat(it).isEqualTo(lockedAndLoadedContestantAdded.copy(id = it.id)) }
            .verifyComplete()
    }

    @Test
    fun `should add two contestant to game and start game`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.just(
            escapeCampCreated,
            lockedAndLoadedContestantAdded
        )

        GameAggregate(escapeCampId, jeepersKeypersContestantId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .addContestant(jeepersKeypersContestant, jeepersKeypersContestantAddedAt)
            .`as`(StepVerifier::create)
            .assertNext { assertThat(it).isEqualTo(jeepersKeypersContestantAdded.copy(id = it.id)) }
            .assertNext { assertThat(it).isEqualTo(escapeCampStarted.copy(id = it.id)) }
            .verifyComplete()
    }

    @Test
    fun `should fail to add contestant to game when 2 contestants exist`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.just(
            escapeCampCreated,
            lockedAndLoadedContestantAdded,
            jeepersKeypersContestantAdded
        )
        val unexpectedContestantId = UUID.randomUUID()

        GameAggregate(escapeCampId, unexpectedContestantId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .addContestant(Contestant(unexpectedContestantId, "unexpectedContestant"), LocalDateTime.now())
            .`as`(StepVerifier::create)
            .verifyErrorMatches { throwable -> throwable is GameException.ContestantNumberLimitExceededException }
    }

    @Test
    fun `should fail to add contestant to game with same name`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.just(
            escapeCampCreated,
            lockedAndLoadedContestantAdded
        )

        GameAggregate(escapeCampId, lockedAndLoadedContestantId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .addContestant(lockedAndLoadedContestant, LocalDateTime.now())
            .`as`(StepVerifier::create)
            .verifyErrorMatches { throwable ->
                throwable is GameException.ContestantNameNotAvailableException || throwable.message.equals(
                    "Contestant with name Lock and loaded already exists"
                )
            }
    }

    @Test
    fun `should fail to add contestant to non-existent game`() {
        every { gameEventStore.loadAllEvents(unknownGameId) } returns Flux.empty()

        GameAggregate(unknownGameId, lockedAndLoadedContestantId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .addContestant(lockedAndLoadedContestant, lockedAndLoadedContestantAddedAt)
            .`as`(StepVerifier::create)
            .verifyErrorMatches { throwable ->
                throwable is GameException.GameNotFoundException || throwable.message.equals(
                    "Game with id 07c905e7-8179-4b59-a65a-510a4e1de4d3 has not been found"
                )
            }
    }
}
