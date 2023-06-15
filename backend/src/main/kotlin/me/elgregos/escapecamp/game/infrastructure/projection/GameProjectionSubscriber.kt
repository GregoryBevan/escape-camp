package me.elgregos.escapecamp.game.infrastructure.projection

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch
import io.github.oshai.KotlinLogging
import jakarta.annotation.PostConstruct
import me.elgregos.escapecamp.game.domain.entity.Game
import me.elgregos.escapecamp.game.domain.event.GameEvent
import me.elgregos.escapecamp.game.domain.event.GameEvent.*
import me.elgregos.reakteves.domain.Event
import me.elgregos.reakteves.domain.JsonConvertible.Companion.fromJson
import me.elgregos.reakteves.infrastructure.ReactorEventBus
import me.elgregos.reakteves.infrastructure.ReactorEventSubscriber
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

private val logger = KotlinLogging.logger {}

@Component
class GameProjectionSubscriber(
    reactorEventBus: ReactorEventBus<UUID>,
    private val gameProjectionRepository: GameProjectionRepository,
) : ReactorEventSubscriber<UUID>(reactorEventBus) {

    @PostConstruct
    fun initialize() {
        subscribe()
    }

    override fun onEvent(event: Event<UUID>): Mono<Void> {
        return Mono.just(event)
            .filter { e -> e is GameEvent }
            .cast(GameEvent::class.java)
            .flatMap {
                when (it) {
                    is GameCreated -> createGame(it)
                    is TeamAdded,
                    is GameStarted,
                    is NextTeamRiddleAssigned,
                    is RiddleSolved -> updateGame(it)
                    else -> Mono.empty()
                }
            }
            .doOnError { error -> logger.error(error) { "An error occurred while processing event" } }
            .then()
    }

    private fun createGame(event: GameCreated) =
        gameProjectionRepository.insert(fromJson(event.event, Game::class.java))


    private fun updateGame(event: GameEvent) =
        gameProjectionRepository.findById(event.aggregateId)
            .flatMap {
                gameProjectionRepository.update(mergeGame(it.toGame(), event), it.sequenceNum!!)
            }

    private fun mergeGame(previousGame: Game, event: GameEvent): Game =
        fromJson(JsonMergePatch.fromJson(event.event).apply(previousGame.toJson()), Game::class.java)
}