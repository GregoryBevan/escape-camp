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
        And("all riddles have been unlocked") {
            repeat(4) {
                gameClient.unlockNextRiddle()
                    .expectStatus().isOk
            }
        }

        When("he unlocks the next riddle") {
            response = gameClient.unlockNextRiddle()
        }

        Then("the riddle is unlocked for all contestants") {
            response!!.expectStatus().isOk
                .expectBody(JsonNode::class.java).consumeWith {
                    assertThat(it.responseBody!!["currentRiddle"]["position"].asText()).isEqualTo("0")
                    assertThat(it.responseBody!!["currentRiddle"]["name"].asText()).isEqualTo("riddle-1")
                    assertThat(it.responseBody!!["currentRiddle"]["content"].asText()).isEqualTo("""
                    # Episode I

                    Un mot à trouver.

                    Pour avoir le début, il faut se rappeler la stratégie de discipline de Monsieur Rachin dans _Les Choristes_ : « action, \_\_\_\_\_ion ».

                    Le mot rime avec le prénom du chanteur et acteur franco-italien qui a joué dans _La Folie des grandeurs_.

                    """.trimIndent())
                }
        }

        Then("the response contains a next riddle unlock not allowed error") {
            response!!.expectStatus().isBadRequest
                .expectBody(JsonNode::class.java).consumeWith {
                    assertThat(it.responseBody!!.get("message").asText()).isEqualTo("Could not get next riddle with this type of contestant enrollment")
                }
        }

        Then("the response contains a riddles all unlocked error") {
            response!!.expectStatus().isBadRequest
                .expectBody(JsonNode::class.java).consumeWith {
                    assertThat(it.responseBody!!.get("message").asText()).isEqualTo("All riddles are already unlocked")
                }
        }
    }
}
