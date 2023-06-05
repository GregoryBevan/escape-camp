package me.elgregos.escapecamp.game.domain.event

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.every
import io.mockk.mockk
import me.elgregos.escapecamp.config.exception.GameException
import me.elgregos.escapecamp.game.domain.entity.*
import me.elgregos.reakteves.domain.EventStore
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class GameAggregateAssignTeamNextRiddleTest {

    private lateinit var gameEventStore: EventStore<GameEvent, UUID>

    @BeforeTest
    fun setup() {
        gameEventStore = mockk()
    }

    @Test
    fun `should assign riddle to team`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.fromIterable(eventsAfterEscapeCampStarted)

        GameAggregate(escapeCampId, lockedAndLoadedTeamId, gameEventStore)
            .assignTeamNextRiddle(lockedAndLoadedFirstRiddleAssignedAt)
            .`as`(StepVerifier::create)
            .assertNext { assertThat(it).isEqualTo(lockedAndLoadedFirstRiddleAssigned.copy(id = it.id)) }
            .verifyComplete()
    }

    @Test
    fun `should fail to assign riddle to team when game is not existing`() {
        every { gameEventStore.loadAllEvents(unknownGameId) } returns Flux.empty()

        GameAggregate(unknownGameId, lockedAndLoadedTeamId, gameEventStore)
            .assignTeamNextRiddle(lockedAndLoadedFirstRiddleAssignedAt)
            .`as`(StepVerifier::create)
            .verifyErrorMatches { throwable ->
                throwable is GameException.GameNotFoundException || throwable.message.equals(
                    "Game with id 07c905e7-8179-4b59-a65a-510a4e1de4d3 has not been found"
                )
            }
    }
}