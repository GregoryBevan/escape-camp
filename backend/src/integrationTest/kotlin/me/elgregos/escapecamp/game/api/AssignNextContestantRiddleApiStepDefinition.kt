package me.elgregos.escapecamp.game.api

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.readValue
import io.cucumber.java8.En
import me.elgregos.reakteves.libs.genericObjectMapper
import org.springframework.beans.factory.annotation.Autowired

class AssignNextContestantRiddleApiStepDefinition: En {

    @Autowired
    private lateinit var gameClient: GameClient

    init {
        When("the contestant requests the next riddle") {
            response = gameClient.requestNextRiddle(currentContestant!!)
        }

        Then("the response contains the riddle") {
            response!!.expectStatus().isOk
                .expectBody(JsonNode::class.java).consumeWith {
                val riddle = genericObjectMapper.readValue<AssignedRiddle>(it.responseBody!!.get("riddle").toString())
                    assertThat(riddle).isEqualTo(AssignedRiddle("riddle-1", """
                    # A la piscine

                    Si vous vous placez au bon endroit,
                    la réponse se révèlera.

                    """.trimIndent()))
                    scenario?.log(
                        """
                        Next riddle for ${currentContestant!!.name}:
                        $riddle
                        """)
                }
        }

        Then("the response contains a previous riddle not solved error") {
            response!!.expectStatus().isBadRequest
                .expectBody(JsonNode::class.java).consumeWith {
                    assertThat(it.responseBody!!.get("message").asText()).isEqualTo("Previous riddle not solved yet")
                }
        }
    }
}
