package me.elgregos.escapecamp.game.api

import jakarta.validation.Valid
import me.elgregos.escapecamp.config.security.AuthenticatedUser
import me.elgregos.escapecamp.config.security.Role
import me.elgregos.escapecamp.config.security.jwt.TokenProvider
import me.elgregos.escapecamp.config.sse.ServerSentEventService
import me.elgregos.escapecamp.game.api.dto.TeamCreationDTO
import me.elgregos.escapecamp.game.application.GameCommand
import me.elgregos.escapecamp.game.application.GameCommandHandler
import me.elgregos.escapecamp.game.application.RiddleService
import me.elgregos.escapecamp.game.domain.event.GameEvent.*
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.*


@RestController
@RequestMapping(
    path = ["/api/games"]
)
class GameController(
    private val gameCommandHandler: GameCommandHandler,
    private val tokenProvider: TokenProvider,
    private val riddleService: RiddleService,
    private val serverSentEventService: ServerSentEventService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ORGANIZER')")
    fun createGame(@AuthenticationPrincipal authenticatedUser: AuthenticatedUser) =
        gameCommandHandler.handle(GameCommand.CreateGame(createdBy = authenticatedUser.id))
            .toMono()
            .map { mapOf(Pair("gameId", it.aggregateId)) }

    @PostMapping("{gameId}/teams")
    @ResponseStatus(HttpStatus.CREATED)
    fun createGame(
        @PathVariable @Valid gameId: UUID,
        @RequestBody @Valid teamCreationDTO: TeamCreationDTO
    ): Mono<Map<String, String>> =
        gameCommandHandler.handle(GameCommand.AddTeam(gameId = gameId, name = teamCreationDTO.name))
            .last()
            .map {
                mapOf(
                    Pair("teamId", "${it.createdBy}"),
                    Pair("accessToken", tokenProvider.generateAccessToken(it.createdBy, teamCreationDTO.name, Role.PLAYER)),
                    Pair("eventType", it.eventType)
                )
            }

    @GetMapping("{gameId}/teams/{teamId}/riddle")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PLAYER')")
    fun assignRiddle(
        @PathVariable @Valid gameId: UUID,
        @PathVariable @Valid teamId: UUID
    ): Mono<Map<String, Map<String, String>>> =
        gameCommandHandler.handle(GameCommand.AssignTeamNextRiddle(gameId, assignedBy = teamId))
            .last()
            .cast(NextTeamRiddleAssigned::class.java)
            .map(NextTeamRiddleAssigned::assignedRiddle)
            .map { riddle ->
                mapOf(Pair("riddle", mapOf(
                            Pair("name", riddle.name),
                            Pair("content", riddleService.retrieveRiddleContent(riddle.name))
                        )
                    )
                )
            }

    @GetMapping(path = ["{id}/events-stream"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    @PreAuthorize("hasAnyAuthority('ORGANIZER','PLAYER')")
    fun eventsStream(@PathVariable @Valid id: UUID) =
        serverSentEventService.sseFlux()
            .filter { it.data()?.get("id")?.asText() == id.toString() }
}