package me.elgregos.escapecamp.game.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping(
    path = ["/api/games"]
)
class GameController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun helloWorld() = Mono.just("Hello world")
}