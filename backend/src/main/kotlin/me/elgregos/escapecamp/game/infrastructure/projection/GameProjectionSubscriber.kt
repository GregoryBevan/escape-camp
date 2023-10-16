package me.elgregos.escapecamp.game.infrastructure.projection

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import me.elgregos.escapecamp.game.domain.entity.Game
import me.elgregos.escapecamp.game.domain.event.GameEvent
import me.elgregos.escapecamp.game.domain.event.GameEvent.*
import me.elgregos.reakteves.domain.JsonConvertible.Companion.fromJson
import me.elgregos.reakteves.domain.event.Event
import me.elgregos.reakteves.domain.projection.ProjectionStore
import me.elgregos.reakteves.infrastructure.event.ReactorEventBus
import me.elgregos.reakteves.infrastructure.event.ReactorEventSubscriber
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

private val logger = KotlinLogging.logger {}

@Component
class GameProjectionSubscriber(
    reactorEventBus: ReactorEventBus<UUID, UUID>,
    private val gameProjectionStore: ProjectionStore<Game, UUID, UUID>,
) : ReactorEventSubscriber<UUID, UUID>(reactorEventBus) {

    @PostConstruct
    fun initialize() {
        subscribe()
    }

    override fun onEvent(event: Event<UUID, UUID>): Mono<Void> {
        return Mono.just(event)
            .filter { e -> e is GameEvent }
            .cast(GameEvent::class.java)
            .flatMap {
                when (it) {
                    is GameCreated -> createGame(it)
                    is ContestantEnrolled,
                    is GameStarted,
                    is NextRiddleUnlocked,
                    is NextContestantRiddleAssigned,
                    is RiddleSolved,
                    is WinnerAnnounced -> updateGame(it)
                }
            }
            .doOnError { error -> logger.error(error) { "An error occurred while processing event" } }
            .then()
    }

    private fun createGame(event: GameCreated) =
        gameProjectionStore.insert(fromJson(event.event, Game::class.java))


    private fun updateGame(event: GameEvent) =
        gameProjectionStore.find(event.aggregateId)
            .flatMap { gameProjectionStore.update(mergeGame(it, event)) }

    private fun mergeGame(previousGame: Game, event: GameEvent): Game =
        fromJson(JsonMergePatch.fromJson(event.event).apply(previousGame.toJson()), Game::class.java)
}
