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

class GameAggregateAssignTeamNextRiddleTest {

    private lateinit var gameEventStore: EventStore<GameEvent, UUID, UUID>

    @BeforeTest
    fun setup() {
        gameEventStore = mockk()
    }

    @Test
    fun `should assign riddle-1 to first registered team after game has started`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.fromIterable(eventsAfterEscapeCampStarted)

        GameAggregate(escapeCampId, lockedAndLoadedTeamId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .assignTeamNextRiddle(lockedAndLoadedFirstRiddleAssignedAt)
            .`as`(StepVerifier::create)
            .assertNext { assertThat(it).isEqualTo(lockedAndLoadedFirstRiddleAssigned.copy(id = it.id)) }
            .verifyComplete()
    }

    @Test
    fun `should assign riddle-2 to second registered team after game has started`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.fromIterable(eventsAfterLockedAndLoadedFirstRiddleAssigned)

        GameAggregate(escapeCampId, jeepersKeypersTeamId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .assignTeamNextRiddle(jeepersKeypersFirstRiddleAssignedAt)
            .`as`(StepVerifier::create)
            .assertNext { assertThat(it).isEqualTo(jeepersKeypersFirstRiddleAssigned.copy(id = it.id)) }
            .verifyComplete()
    }

    @Test
    fun `should assign riddle-2 to first registered team after first riddle solved`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.fromIterable(eventsAfterJeepersKeypersFirstRiddleSolved)

        GameAggregate(escapeCampId, jeepersKeypersTeamId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .assignTeamNextRiddle(jeepersKeypersSecondRiddleAssignedAt)
            .`as`(StepVerifier::create)
            .assertNext { assertThat(it).isEqualTo(jeepersKeypersSecondRiddleAssigned.copy(id = it.id)) }
            .verifyComplete()
    }

    @Test
    fun `should fail to assign riddle to first registered team if game not yet started`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.fromIterable(eventsAfterLockedAndLoadedAdded)

        GameAggregate(escapeCampId, lockedAndLoadedTeamId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .assignTeamNextRiddle(lockedAndLoadedFirstRiddleAssignedAt)
            .`as`(StepVerifier::create)
            .verifyErrorMatches { throwable ->  throwable is GameException.GameNotStartedException && throwable.message.equals(
                "Game has not yet started. Wait for other teams to be added"
            ) }
    }

    @Test
    fun `should fail to assign riddle to first registered team if previous riddle not solved`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.fromIterable(eventsAfterLockedAndLoadedFirstRiddleAssigned)

        GameAggregate(escapeCampId, lockedAndLoadedTeamId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .assignTeamNextRiddle(lockedAndLoadedFirstRiddleAssignedAt.plusSeconds(30))
            .`as`(StepVerifier::create)
            .verifyErrorMatches { throwable ->  throwable is GameException.PreviousRiddleNotSolvedException && throwable.message.equals(
                "Previous riddle not solved yet"
            ) }
    }

    @Test
    fun `should fail to assign riddle to a non-existing team`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.fromIterable(eventsAfterLockedAndLoadedFirstRiddleAssigned)

        GameAggregate(escapeCampId, unknownTeamId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .assignTeamNextRiddle(lockedAndLoadedFirstRiddleAssignedAt.plusSeconds(30))
            .`as`(StepVerifier::create)
            .verifyErrorMatches { throwable ->  throwable is GameException.TeamNotFoundException && throwable.message.equals(
                "Team with id 4f5ddf39-4a5c-4317-b4a6-db2d6c50dd18 has not been found"
            ) }
    }

    @Test
    fun `should fail to assign riddle to team when game is not existing`() {
        every { gameEventStore.loadAllEvents(unknownGameId) } returns Flux.empty()

        GameAggregate(unknownGameId, lockedAndLoadedTeamId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .assignTeamNextRiddle(lockedAndLoadedFirstRiddleAssignedAt)
            .`as`(StepVerifier::create)
            .verifyErrorMatches { throwable ->
                throwable is GameException.GameNotFoundException && throwable.message.equals(
                    "Game with id 07c905e7-8179-4b59-a65a-510a4e1de4d3 has not been found"
                )
            }
    }
}
