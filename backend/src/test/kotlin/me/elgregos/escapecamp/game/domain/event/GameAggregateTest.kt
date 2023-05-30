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
import java.time.LocalDateTime
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
    fun `should create new game`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.empty()
        GameAggregate(escapeCampId, escapeCampCreatorId, gameEventStore).createGame(escapeCampCreatedAt)
            .`as`(StepVerifier::create)
            .assertNext { assertThat(it).isEqualTo(escapeCampCreated.copy(id = it.id)) }
            .verifyComplete()
    }

    @Test
    fun `should add first team to game`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.just(escapeCampCreated)
        GameAggregate(escapeCampId, lockedAndLoadedTeamId, gameEventStore).addTeam(
            lockedAndLoadedTeam,
            lockedAndLoadedTeamAddedAt
        )
            .`as`(StepVerifier::create)
            .assertNext { assertThat(it).isEqualTo(lockedAndLoadedTeamAdded.copy(id = it.id)) }
            .verifyComplete()
    }

    @Test
    fun `should fail to add team to game when 4 teams exists`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.just(
            escapeCampCreated,
            lockedAndLoadedTeamAdded,
            jeepersKeypersTeamAdded,
            theEscapePeasTeamAdded,
            sherUnlockTeamAdded
        )
        val unexpectedTeamId = UUID.randomUUID()
        GameAggregate(escapeCampId, unexpectedTeamId, gameEventStore).addTeam(
            Team(unexpectedTeamId, "unexpectedTeam"), LocalDateTime.now()
        )
            .`as`(StepVerifier::create)
            .verifyErrorMatches { throwable -> throwable is GameException.TeamNumberLimitExceededException }
    }

    @Test
    fun `should fail to add team to game with same name`() {
        every { gameEventStore.loadAllEvents(escapeCampId) } returns Flux.just(
            escapeCampCreated,
            lockedAndLoadedTeamAdded
        )
        GameAggregate(escapeCampId, lockedAndLoadedTeamId, gameEventStore).addTeam(
            lockedAndLoadedTeam,
            LocalDateTime.now()
        )
            .`as`(StepVerifier::create)
            .verifyErrorMatches { throwable ->
                throwable is GameException.TeamNameNotAvailableException || throwable.message.equals(
                    "Team with name Lock and loaded already exists"
                )
            }
    }

    @Test
    fun `should fail to add team to non-existent game`() {
        every { gameEventStore.loadAllEvents(unknownGameId) } returns Flux.empty()
        GameAggregate(unknownGameId, lockedAndLoadedTeamId, gameEventStore).addTeam(
            lockedAndLoadedTeam,
            lockedAndLoadedTeamAddedAt
        )
            .`as`(StepVerifier::create)
            .verifyErrorMatches { throwable ->
                throwable is GameException.GameNotFoundException || throwable.message.equals(
                    "Game with id 07c905e7-8179-4b59-a65a-510a4e1de4d3 has not been found"
                )
            }
    }
}