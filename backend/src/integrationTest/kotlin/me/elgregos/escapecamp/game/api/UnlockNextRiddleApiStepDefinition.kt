package me.elgregos.escapecamp.game.api

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.fasterxml.jackson.databind.JsonNode
import io.cucumber.java8.En
import org.springframework.beans.factory.annotation.Autowired

class UnlockNextRiddleApiStepDefinition: En {

    @Autowired
    private lateinit var gameClient: GameClient

    init {
        When("he unlocks the next riddle") {
            response = gameClient.unlockNextRiddle()
        }

        Then("the riddle is unlocked for all contestants") {
            response!!.expectStatus().isOk
                .expectBody().isEmpty
        }

        Then("the response contains a next riddle unlock not allowed error") {
            response!!.expectStatus().isBadRequest
                .expectBody(JsonNode::class.java).consumeWith {
                    assertThat(it.responseBody!!.get("message").asText()).isEqualTo("Could not get next riddle with this type of contestant enrollment")
                }
        }
    }
}
