package me.elgregos.escapecamp.features

import io.cucumber.java8.En
import io.cucumber.java8.Scenario
import me.elgregos.escapecamp.game.api.GameClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.reactive.server.WebTestClient

var scenario: Scenario? = null
var organizerJwt: String? = null
var response: WebTestClient.ResponseSpec? = null

class SharedGivenStepDefinition : En {

    @Autowired
    private lateinit var gameClient: GameClient

    init {
        Before { s: Scenario ->
            scenario = s
        }

        Given("an authenticated organizer") {
            gameClient.authenticateOrganizer()
        }

        Given("an unauthenticated user") {
            organizerJwt = null
        }
    }
}
