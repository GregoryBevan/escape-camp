package me.elgregos.escapecamp.game.domain.event

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.every
import io.mockk.mockk
import me.elgregos.reakteves.domain.EventStore
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class GameAggregateTest {

    private lateinit var gameEventStore: EventStore<GameEvent, UUID>

    @BeforeTest
    fun setup() {
        gameEventStore = mockk()
    }

    @Test
    fun `should start new game`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.empty()
        GameAggregate(escapeCampId, escapeCampCreatorId, gameEventStore).startGame(escapeCampCreatedAt)
            .`as`(StepVerifier::create)
            .assertNext { assertThat(it).isEqualTo(escapeCampStarted.copy(id = it.id)) }
            .verifyComplete()
    }
}