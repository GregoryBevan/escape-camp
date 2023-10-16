package me.elgregos.escapecamp.game.api

import com.fasterxml.jackson.databind.JsonNode
import io.cucumber.java8.En
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired

class GameLeaderboardApiStepDefinition : En {

    @Autowired
    private lateinit var gameClient: GameClient

    init {

        When("he requests the game leaderboard") {
            response = gameClient.gameLeaderboard()
        }

        Then("the response contains the leaderboard") {
            response!!.expectStatus().isOk
                .expectBody(JsonNode::class.java).consumeWith {
                    assertThat(it.responseBody!!.toPrettyString()).isEqualTo("""
                        {
                          "gameId" : "$gameId",
                          "lines" : [ {
                            "contestantName" : "Locked and Loaded",
                            "solvedRiddlesNumber" : 0,
                            "timeToSolve" : "PT0S"
                          }, {
                            "contestantName" : "Jeepers Keypers",
                            "solvedRiddlesNumber" : 0,
                            "timeToSolve" : "PT0S"
                          }, {
                            "contestantName" : "The Escape Peas",
                            "solvedRiddlesNumber" : 0,
                            "timeToSolve" : "PT0S"
                          }, {
                            "contestantName" : "Sher-unlock",
                            "solvedRiddlesNumber" : 0,
                            "timeToSolve" : "PT0S"
                          } ]
                        }
                    """.trimIndent())
                    scenario!!.log(it.responseBody!!.toPrettyString())
                }
        }
    }

}
