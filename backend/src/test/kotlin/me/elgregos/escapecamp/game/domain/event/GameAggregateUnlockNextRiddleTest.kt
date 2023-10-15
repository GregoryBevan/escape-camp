package me.elgregos.escapecamp.game.domain.event

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.every
import io.mockk.mockk
import me.elgregos.escapecamp.config.exception.GameException
import me.elgregos.escapecamp.config.security.organizerId
import me.elgregos.escapecamp.game.domain.entity.*
import me.elgregos.escapecamp.game.domain.service.MockedRiddleSolutionChecker
import me.elgregos.reakteves.domain.event.EventStore
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class GameAggregateUnlockNextRiddleTest {

    private lateinit var gameEventStore: EventStore<GameEvent, UUID, UUID>

    @BeforeTest
    fun setup() {
        gameEventStore = mockk()
    }

    @Test
    fun `should unlock first riddle and start game if none already unlocked`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.fromIterable(eventsAfterAllContestantEnrolledInUnlimitedEnrollmentGame)

        GameAggregate(escapeCampId, organizerId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .unlockNextRiddle(firstRiddleUnlockedAt)
            .`as`(StepVerifier::create)
            .assertNext { assertThat(it).isEqualTo(firstRiddleUnlocked.copy(id = it.id)) }
            .assertNext { assertThat(it).isEqualTo(unlimitedEnrollmentEscapeCampStarted.copy(id = it.id)) }
            .verifyComplete()
    }

    @Test
    fun `should fail to unlock riddle if enrollment type is limited`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.fromIterable(eventsAfterAllContestantEnrolled)

        GameAggregate(escapeCampId, organizerId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .unlockNextRiddle(firstRiddleUnlockedAt)
            .`as`(StepVerifier::create)
            .verifyErrorMatches { error -> error is GameException.RiddleUnlockedNotAllowedException && error.message == "Could not get next riddle with this type of contestant enrollment" }
    }

    @Test
    fun `should fail to unlock riddle if all riddle are already unlocked`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.fromIterable(eventsAfterSecondRiddleUnlocked)

        GameAggregate(escapeCampId, organizerId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .unlockNextRiddle(secondRiddleUnlockedAt)
            .`as`(StepVerifier::create)
            .verifyErrorMatches { error -> error is GameException.AllRiddlesAlreadyUnlockedException && error.message == "All riddles are already unlocked" }
    }

    @Test
    fun `should fail to unlock riddle of non-existent game`() {
        every { gameEventStore.loadAllEvents(unknownGameId) } returns Flux.empty()

        GameAggregate(unknownGameId, organizerId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .unlockNextRiddle(firstRiddleUnlockedAt)
            .`as`(StepVerifier::create)
            .verifyErrorMatches { throwable ->
                throwable is GameException.GameNotFoundException || throwable.message.equals(
                    "Game with id 07c905e7-8179-4b59-a65a-510a4e1de4d3 has not been found"
                )
            }
    }
}
