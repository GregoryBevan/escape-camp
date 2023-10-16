package me.elgregos.escapecamp.game.domain.event

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.every
import io.mockk.mockk
import me.elgregos.escapecamp.config.security.organizerId
import me.elgregos.escapecamp.game.domain.entity.EnrollmentType
import me.elgregos.escapecamp.game.domain.entity.escapeCampCreatedAt
import me.elgregos.escapecamp.game.domain.entity.escapeCampId
import me.elgregos.escapecamp.game.domain.entity.riddles
import me.elgregos.escapecamp.game.domain.service.MockedRiddleSolutionChecker
import me.elgregos.reakteves.domain.event.EventStore
import org.junit.jupiter.api.Named.named
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.*
import java.util.stream.Stream
import kotlin.test.BeforeTest

class GameAggregateCreateGameTest {

    private lateinit var gameEventStore: EventStore<GameEvent, UUID, UUID>

    @BeforeTest
    fun setup() {
        gameEventStore = mockk()
    }

    @ParameterizedTest
    @MethodSource("createGameTestCases")
    fun `should create new game`(enrollmentType: EnrollmentType, expectedEvent: GameEvent.GameCreated) {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.empty()

        GameAggregate(escapeCampId, organizerId, MockedRiddleSolutionChecker(riddles), gameEventStore)
            .createGame(enrollmentType, riddles, escapeCampCreatedAt)
            .`as`(StepVerifier::create)
            .assertNext { assertThat(it).isEqualTo(expectedEvent.copy(id = it.id)) }
            .verifyComplete()
    }

    companion object {
        @JvmStatic
        fun createGameTestCases() = Stream.of(
            Arguments.of(named("with LIMITED_TO_RIDDLE_NUMBER enrollment type", EnrollmentType.LIMITED_TO_RIDDLE_NUMBER), escapeCampCreated),
            Arguments.of(named("with UNLIMITED enrollment type", EnrollmentType.UNLIMITED), unlimitedEnrollmentEscapeCampCreated)
        )
    }
}
