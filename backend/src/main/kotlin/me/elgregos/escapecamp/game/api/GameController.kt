package me.elgregos.escapecamp.game.api

import me.elgregos.escapecamp.config.security.AuthenticatedUser
import me.elgregos.escapecamp.game.application.GameCommand
import me.elgregos.escapecamp.game.application.GameCommandHandler
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(
    path = ["/api/games"]
)
class GameController(val gameCommandHandler: GameCommandHandler) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
//    @PreAuthorize("hasAuthority('ORGANIZER')")
    fun createGame(@AuthenticationPrincipal  authenticatedUser: AuthenticatedUser) =
        gameCommandHandler.handle(GameCommand.CreateGame(createdBy = authenticatedUser.id))
            .map { mapOf(Pair("gameId", it.aggregateId)) }
}