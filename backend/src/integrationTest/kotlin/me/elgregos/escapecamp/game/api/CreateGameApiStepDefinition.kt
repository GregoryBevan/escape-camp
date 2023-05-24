package me.elgregos.escapecamp.game.api

import assertk.assertThat
import assertk.assertions.isNotNull
import com.fasterxml.jackson.databind.JsonNode
import io.cucumber.java8.En
import me.elgregos.escapecamp.features.scenario
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.reactive.server.WebTestClient

class CreateGameApiStepDefinition : En {

    @Autowired
    private lateinit var gameClient: GameClient

    private var response: WebTestClient.ResponseSpec? = null

    init {

        When("he creates an escape camp") {
            response = gameClient.createGame()
        }

        Then("the game is created") {
            response!!.expectStatus().isCreated
                .expectBody(JsonNode::class.java).consumeWith {
                    val gameId = it.responseBody?.get("gameId")
                    scenario?.log("Game identifier $gameId")
                    assertThat(gameId).isNotNull()
                }
        }
    }

}