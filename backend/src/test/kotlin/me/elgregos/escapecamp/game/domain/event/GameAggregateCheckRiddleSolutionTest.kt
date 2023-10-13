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
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class GameAggregateCheckRiddleSolutionTest {

    private lateinit var gameEventStore: EventStore<GameEvent, UUID, UUID>

    @BeforeTest
    fun setup() {
        gameEventStore = mockk()
    }

    @Test
    fun `should succeed to check right submitted solution`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.fromIterable(eventsAfterAllFirstRiddleAssigned)

        GameAggregate(escapeCampId, jeepersKeypersContestantId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .checkRiddleSolution("riddle-2", "solution-2", jeepersKeypersFirstRiddleSolvedAt)
            .`as`(StepVerifier::create)
            .assertNext { assertThat(it).isEqualTo(jeepersKeypersFirstRiddleSolved.copy(id = it.id)) }
            .verifyComplete()
    }

    @Test
    fun `should announce first contestant to solve all riddle has the winner`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.fromIterable(
            eventsAfterLockedAndLoadedSecondRiddleAssigned
        )

        GameAggregate(escapeCampId, jeepersKeypersContestantId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .checkRiddleSolution("riddle-1", "solution-1", jeepersKeypersSecondRiddleSolvedAt)
            .`as`(StepVerifier::create)
            .assertNext { assertThat(it).isEqualTo(jeepersKeypersSecondRiddleSolved.copy(id = it.id)) }
            .assertNext { assertThat(it).isEqualTo(escapeCampWinnerAnnounced.copy(id = it.id)) }
            .verifyComplete()
    }

    @Test
    fun `should not change winner after another contestant has solved all riddle`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.fromIterable(eventsAfterWinnerAnnounced)

        GameAggregate(escapeCampId, lockedAndLoadedContestantId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .checkRiddleSolution("riddle-2", "solution-2", lockedAndLoadedSecondRiddleSolvedAt)
            .`as`(StepVerifier::create)
            .assertNext { assertThat(it).isEqualTo(lockedAndLoadedSecondRiddleSolved.copy(id = it.id)) }
            .verifyComplete()
    }

    @Test
    fun `should fail if submitted solution is incorrect`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.fromIterable(eventsAfterAllFirstRiddleAssigned)

        GameAggregate(escapeCampId, jeepersKeypersContestantId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .checkRiddleSolution("riddle-2", "wrong", jeepersKeypersFirstRiddleSolvedAt)
            .`as`(StepVerifier::create)
            .verifyErrorMatches { throwable ->
                throwable is GameException.IncorrectSolutionException
                        && throwable.message == "The submitted solution \"wrong\" for riddle riddle-2 is incorrect"
            }
    }

    @Test
    fun `should fail if contestant last unsolved riddle is not the one checked`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.fromIterable(eventsAfterAllFirstRiddleAssigned)

        GameAggregate(escapeCampId, jeepersKeypersContestantId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .checkRiddleSolution("riddle-1", "solution-1", jeepersKeypersFirstRiddleSolvedAt)
            .`as`(StepVerifier::create)
            .verifyErrorMatches { throwable ->
                throwable is GameException.UnexpectedRiddleSolutionException
                        && throwable.message == "The riddle riddle-1 doesn't correspond to last unsolved riddle of the contestant"
            }
    }

    @Test
    fun `should fail to check submitted solution for non-existent game`() {
        every { gameEventStore.loadAllEvents(unknownGameId) } returns Flux.empty()

        GameAggregate(unknownGameId, jeepersKeypersContestantId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .checkRiddleSolution("riddle-3", "", jeepersKeypersFirstRiddleSolvedAt)
            .`as`(StepVerifier::create)
            .verifyErrorMatches { throwable ->
                throwable is GameException.GameNotFoundException
                        && throwable.message == "Game with id 07c905e7-8179-4b59-a65a-510a4e1de4d3 has not been found"
            }
    }

}
