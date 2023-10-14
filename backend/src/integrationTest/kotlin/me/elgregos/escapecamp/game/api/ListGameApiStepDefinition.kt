package me.elgregos.escapecamp.game.api

import com.fasterxml.jackson.databind.node.ArrayNode
import io.cucumber.java8.En
import me.elgregos.escapecamp.features.response
import me.elgregos.escapecamp.features.scenario
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired

class ListGameApiStepDefinition : En {

    @Autowired
    private lateinit var gameClient: GameClient

    init {

        When("he requests the game list") {
            response = gameClient.listGame()
        }

        Then("the response contains all games") {
            response!!.expectStatus().isOk
                .expectBody(ArrayNode::class.java).consumeWith {
                    assertThat(it.responseBody!!.size()).isEqualTo(11)
                    scenario!!.log(it.responseBody!!.toPrettyString())
                }
        }
    }

}
