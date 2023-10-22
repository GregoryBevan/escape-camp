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
                    # Episode I

                    Un mot à trouver.

                    Pour avoir le début, il faut se rappeler la stratégie de discipline de Monsieur Rachin dans _Les Choristes_ : « action, \_\_\_\_\_ion ».

                    Le mot rime avec le prénom du chanteur et acteur franco-italien qui a joué dans _La Folie des grandeurs_.

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
