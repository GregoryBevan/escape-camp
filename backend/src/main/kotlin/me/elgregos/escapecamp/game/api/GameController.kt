package me.elgregos.escapecamp.game.api

import jakarta.validation.Valid
import me.elgregos.escapecamp.config.security.AuthenticatedUser
import me.elgregos.escapecamp.config.security.Role
import me.elgregos.escapecamp.config.security.jwt.TokenProvider
import me.elgregos.escapecamp.game.api.dto.TeamCreationDTO
import me.elgregos.escapecamp.game.application.GameCommand
import me.elgregos.escapecamp.game.application.GameCommandHandler
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(
    path = ["/api/games"]
)
class GameController(val gameCommandHandler: GameCommandHandler, val tokenProvider: TokenProvider) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ORGANIZER')")
    fun createGame(@AuthenticationPrincipal authenticatedUser: AuthenticatedUser) =
        gameCommandHandler.handle(GameCommand.CreateGame(createdBy = authenticatedUser.id))
            .map { mapOf(Pair("gameId", it.aggregateId)) }

    @PostMapping("{id}/teams")
    @ResponseStatus(HttpStatus.CREATED)
    fun createGame(
        @PathVariable @Valid id: UUID,
        @RequestBody @Valid teamCreationDTO: TeamCreationDTO
    ) =
        gameCommandHandler.handle(GameCommand.AddTeam(gameId = id, name = teamCreationDTO.name))
            .map {
                mapOf(
                    Pair("teamId", it.createdBy),
                    Pair(
                        "accessToken",
                        tokenProvider.generateAccessToken(it.createdBy, teamCreationDTO.name, Role.PLAYER)
                    )
                )
            }
}