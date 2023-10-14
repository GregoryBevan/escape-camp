package me.elgregos.escapecamp.game.api

import assertk.assertThat
import assertk.assertions.isNotNull
import com.fasterxml.jackson.databind.JsonNode
import io.cucumber.java8.En
import me.elgregos.escapecamp.features.response
import me.elgregos.escapecamp.features.scenario
import org.springframework.beans.factory.annotation.Autowired

class CreateGameApiStepDefinition : En {

    @Autowired
    private lateinit var gameClient: GameClient

    init {

        When("he creates an escape camp") {  ->
            response = gameClient.createGame()
        }

        When("he creates an escape camp with enrollment type {string}") { enrollmentType: String ->
            response = gameClient.createGame(enrollmentType)
        }

        Then("the game is created") {
            response!!.expectStatus().isCreated
                .expectBody(JsonNode::class.java).consumeWith {
                    val gameId = it.responseBody?.get("gameId")?.asText()
                    assertThat(gameId).isNotNull()
                    scenario?.log("Game identifier $gameId")
                }
        }
    }

}
