package me.elgregos.escapecamp.game.api

import com.fasterxml.jackson.databind.JsonNode
import io.cucumber.java8.En
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import java.time.Duration

class GameLeaderboardApiStepDefinition : En {

    @Autowired
    private lateinit var gameClient: GameClient

    init {

        When("he requests the game leaderboard") {
            Thread.sleep(1000)
            response = gameClient.gameLeaderboard()
        }

        Then("the response contains an empty leaderboard") {
            response!!.expectStatus().isOk
                .expectBody(JsonNode::class.java).consumeWith {
                    assertThat(it.responseBody!!.toPrettyString()).isEqualTo(
                        """
                        {
                          "gameId" : "$gameId",
                          "lines" : [ ]
                        }
                         """.trimIndent()
                    )
                }
        }

        Then("the response contains the leaderboard in order of enrollment") {
            response!!.expectStatus().isOk
                .expectBody(JsonNode::class.java).consumeWith {
                    scenario!!.log(it.responseBody!!.toPrettyString())
                    assertThat(it.responseBody!!.toPrettyString()).isEqualTo(
                        """
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
                }
        }

        Then("the response contains the leaderboard in order of resolution") {
            response!!.expectStatus().isOk
                .expectBody(JsonNode::class.java).consumeWith {
                    val responseBody = it.responseBody!!
                    scenario!!.log(responseBody.toPrettyString())
                    assertThat(responseBody["gameId"].asText()).isEqualTo("$gameId")
                    val lines = responseBody["lines"]
                    assertThat(lines[0]["contestantName"].asText()).isEqualTo("Jeepers Keypers")
                    assertThat(lines[0]["solvedRiddlesNumber"].asInt()).isEqualTo(1)
                    assertThat(Duration.parse(lines[0]["timeToSolve"].asText())).isGreaterThan(Duration.ofSeconds(1))
                    assertThat(lines[1]["contestantName"].asText()).isEqualTo("Locked and Loaded")
                    assertThat(lines[2]["contestantName"].asText()).isEqualTo("The Escape Peas")
                    assertThat(lines[3]["contestantName"].asText()).isEqualTo("Sher-unlock")
                }
        }
    }

}
