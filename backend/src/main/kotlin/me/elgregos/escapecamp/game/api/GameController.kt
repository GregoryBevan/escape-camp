package me.elgregos.escapecamp.game.api

import jakarta.validation.Valid
import me.elgregos.escapecamp.config.security.AuthenticatedUser
import me.elgregos.escapecamp.config.security.Role
import me.elgregos.escapecamp.config.security.jwt.TokenProvider
import me.elgregos.escapecamp.config.sse.ServerSentEventService
import me.elgregos.escapecamp.game.api.dto.ContestantCreationDTO
import me.elgregos.escapecamp.game.api.dto.GameDTO
import me.elgregos.escapecamp.game.api.dto.RiddleSolutionDTO
import me.elgregos.escapecamp.game.application.GameCommand.*
import me.elgregos.escapecamp.game.application.GameCommandHandler
import me.elgregos.escapecamp.game.application.GameService
import me.elgregos.escapecamp.game.domain.event.GameEvent.NextContestantRiddleAssigned
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
    private val gameService: GameService,
    private val serverSentEventService: ServerSentEventService,
    private val riddles: List<Pair<String, String>>
) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ORGANIZER')")
    fun games() = gameService.games()

    @GetMapping("{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ORGANIZER')")
    fun game(@PathVariable @Valid gameId: UUID) = gameService.game(gameId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ORGANIZER')")
    fun createGame(@AuthenticationPrincipal authenticatedUser: AuthenticatedUser, @RequestBody gameDTO: GameDTO) =
        gameCommandHandler.handle(
            CreateGame(
                createdBy = authenticatedUser.id,
                riddles = riddles,
                enrollmentType = gameDTO.enrollmentType
            )
        )
            .toMono()
            .map { mapOf(Pair("gameId", it.aggregateId)) }

    @PostMapping("{gameId}/contestants")
    @ResponseStatus(HttpStatus.CREATED)
    fun enrollContestant(
        @PathVariable @Valid gameId: UUID,
        @RequestBody @Valid contestantCreationDTO: ContestantCreationDTO
    ): Mono<Map<String, String>> =
        gameCommandHandler.handle(
            EnrollContestant(
                gameId = gameId,
                name = contestantCreationDTO.name
            )
        )
            .last()
            .map {
                mapOf(
                    Pair("contestantId", "${it.createdBy}"),
                    Pair("accessToken", tokenProvider.generateAccessToken(it.createdBy, contestantCreationDTO.name, Role.CONTESTANT)),
                    Pair("eventType", it.eventType)
                )
            }

    @PostMapping("{gameId}/riddles/next")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ORGANIZER')")
    fun unlockNextRiddle(
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
        @PathVariable @Valid gameId: UUID
    ): Mono<Void> =
        gameCommandHandler.handle(UnlockNextRiddle(gameId, unlockedBy = authenticatedUser.id))
            .then()

    @GetMapping("{gameId}/contestants/{contestantId}/riddle")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('CONTESTANT')")
    fun assignRiddle(
        @PathVariable @Valid gameId: UUID,
        @PathVariable @Valid contestantId: UUID
    ): Mono<Map<String, Map<String, String>>> =
        gameCommandHandler.handle(AssignContestantNextRiddle(gameId, assignedBy = contestantId))
            .last()
            .cast(NextContestantRiddleAssigned::class.java)
            .map(NextContestantRiddleAssigned::assignedRiddle)
            .map { riddle ->
                mapOf(Pair("riddle", mapOf(
                            Pair("name", riddle.name),
                            Pair("content", gameService.retrieveRiddleContent(riddle.name))
                        )
                    )
                )
            }

    @PostMapping("{gameId}/contestants/{contestantId}/riddle/{riddleName}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('CONTESTANT')")
    fun submitRiddleSolution(
        @PathVariable @Valid gameId: UUID,
        @PathVariable @Valid contestantId: UUID,
        @PathVariable @Valid riddleName: String,
        @RequestBody @Valid riddleSolutionDTO: RiddleSolutionDTO
    ): Mono<Void> =
        gameCommandHandler.handle(SubmitRiddleSolution(
                gameId,
                submittedBy = contestantId,
                riddleName = riddleName,
                solution = riddleSolutionDTO.solution
            ))
            .then()

    @GetMapping(path = ["{id}/events-stream"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    @PreAuthorize("hasAnyAuthority('ORGANIZER','CONTESTANT')")
    fun eventsStream(@PathVariable @Valid id: UUID) =
        serverSentEventService.sseFlux()
            .filter { it.data()?.get("id")?.asText() == id.toString() }
}
