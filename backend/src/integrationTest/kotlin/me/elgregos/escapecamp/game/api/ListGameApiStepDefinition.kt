package me.elgregos.escapecamp.game.api

import io.cucumber.java8.En
import org.springframework.beans.factory.annotation.Autowired

class ListGameApiStepDefinition : En {

    @Autowired
    private lateinit var gameClient: GameClient

    init {

        When("he requests the game list") {
//            response = gameClient.listGame()
        }

        Then("the response contains all games") {
//            response!!.expectStatus().isCreated
//                .expectBody(ArrayNode::class.java).consumeWith {
//                    assertThat(it.responseBody!!.size()).isEqualTo(7)
//                    scenario!!.log(it.responseBody!!.toPrettyString())
//                }
        }
    }

}