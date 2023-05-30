package me.elgregos.escapecamp.features

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.fasterxml.jackson.databind.JsonNode
import io.cucumber.java8.En


class SharedThenStepDefinition : En {

    init {

        Then("an authentication error is returned") {
            response!!.expectStatus()
                .isUnauthorized()
        }

        Then("the response contains a game not found error") {
            response!!.expectStatus().isNotFound()
                .expectBody(JsonNode::class.java).consumeWith {
                    assertThat(
                        it.responseBody!!.get("message").asText()
                    ).isEqualTo("Game with id $gameId has not been found")
                }
        }
    }
}
