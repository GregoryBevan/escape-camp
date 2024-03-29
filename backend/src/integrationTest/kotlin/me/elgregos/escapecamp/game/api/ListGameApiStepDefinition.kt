package me.elgregos.escapecamp.game.api

import com.fasterxml.jackson.databind.node.ArrayNode
import io.cucumber.java8.En
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired

class ListGameApiStepDefinition : En {

    @Autowired
    private lateinit var gameClient: GameClient

    init {

        When("he requests the game list") {
            response = gameClient.listGames()
        }

        Then("the response contains all games") {
            response!!.expectStatus().isOk
                .expectBody(ArrayNode::class.java).consumeWith {
                    assertThat(it.responseBody!!.size()).isEqualTo(20)
                    scenario!!.log(it.responseBody!!.toPrettyString())
                }
        }
    }

}
