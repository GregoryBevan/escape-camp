package me.elgregos.escapecamp.game.api

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.fasterxml.jackson.databind.JsonNode
import io.cucumber.java8.En
import me.elgregos.escapecamp.features.scenario
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.*

class AddTeamApiStepDefinition : En {

    @Autowired
    private lateinit var gameClient: GameClient

    private lateinit var gameId: UUID
    private lateinit var response: WebTestClient.ResponseSpec
    private lateinit var responseBody: JsonNode

    init {

        Given("a player with game identifier") {
            gameClient.authenticateOrganizer()
            gameClient.createGame()
                .expectStatus().isCreated
                .expectBody(JsonNode::class.java).consumeWith {
                    gameId = UUID.fromString(
                        it.responseBody?.get("gameId")?.asText() ?: throw Exception("Error while getting game id")
                    )
                    assertThat(gameId).isNotNull()
                    scenario?.log("Game identifier $gameId")
                }
        }

        And("a team with name {string} has been added to the game") { teamName: String ->
            gameClient.addTeam(gameId, teamName)
                .expectBody(JsonNode::class.java).consumeWith {
                    responseBody = it.responseBody!!
                    assertThat(responseBody.get("teamId")).isNotNull()
                }
        }

        When("he adds his team to the game with name {string}") { teamName: String ->
            response = gameClient.addTeam(gameId, teamName)
        }

        Then("the team is added") {
            response.expectStatus().isCreated
                .expectBody(JsonNode::class.java).consumeWith {
                    responseBody = it.responseBody!!
                    val teamId = responseBody.get("teamId")
                    assertThat(teamId).isNotNull()
                    scenario?.log("Team identifier $teamId")
                }
        }

        Then("a token is returned to continue the game") {
            val accessToken = responseBody.get("accessToken")
            assertThat(accessToken).isNotNull()
            scenario?.log("Team access token $accessToken")
        }

        Then("the response contains a team name not available error") {
            response.expectStatus().isBadRequest
                .expectBody(JsonNode::class.java).consumeWith {
                    assertThat(
                        it.responseBody!!.get("message").asText()
                    ).isEqualTo("Team with name Locked and loaded already exists")
                }
        }
    }

}