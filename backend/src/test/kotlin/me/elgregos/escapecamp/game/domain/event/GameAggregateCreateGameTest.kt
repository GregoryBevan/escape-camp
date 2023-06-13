package me.elgregos.escapecamp.game.domain.event

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.every
import io.mockk.mockk
import me.elgregos.escapecamp.config.security.organizerId
import me.elgregos.escapecamp.game.domain.entity.escapeCampCreatedAt
import me.elgregos.escapecamp.game.domain.entity.escapeCampId
import me.elgregos.escapecamp.game.domain.service.MockedRiddleSolutionChecker
import me.elgregos.reakteves.domain.EventStore
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class GameAggregateCreateGameTest {

    private lateinit var gameEventStore: EventStore<GameEvent, UUID>

    @BeforeTest
    fun setup() {
        gameEventStore = mockk()
    }

    @Test
    fun `should create new game`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.empty()

        GameAggregate(escapeCampId, organizerId, MockedRiddleSolutionChecker(), gameEventStore)
            .createGame(escapeCampCreatedAt)
            .`as`(StepVerifier::create)
            .assertNext { assertThat(it).isEqualTo(escapeCampCreated.copy(id = it.id)) }
            .verifyComplete()
    }
}