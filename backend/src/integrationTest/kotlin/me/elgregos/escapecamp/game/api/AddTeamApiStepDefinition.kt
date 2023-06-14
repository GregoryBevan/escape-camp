package me.elgregos.escapecamp.game.api

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.fasterxml.jackson.databind.JsonNode
import io.cucumber.datatable.DataTable
import io.cucumber.java8.En
import me.elgregos.escapecamp.features.*
import org.springframework.beans.factory.annotation.Autowired
import reactor.test.StepVerifier
import java.util.*

class AddTeamApiStepDefinition : En {

    @Autowired
    private lateinit var gameClient: GameClient

    private lateinit var responseBody: JsonNode

    init {

        Given("a team player with game identifier") {
            createGame(gameClient)
        }

        Given("a team player with an unknown game identifier") {
            gameId = UUID.randomUUID()
            assertThat(gameId).isNotNull()
            scenario?.log("Unknown ame identifier $gameId")
        }

        And("a team with name {string} has been added to the game") { teamName: String ->
            gameClient.addTeam(teamName)
                .expectBody(JsonNode::class.java).consumeWith {
                    responseBody = it.responseBody!!
                    assertThat(responseBody.get("teamId")).isNotNull()
                }
        }

        When("he adds his team to the game with name {string}") { teamName: String ->
            response = gameClient.addTeam(teamName)
        }

        When("{int} teams have been added to the game") { _: Int, teamNamesTable: DataTable ->
            teamNamesTable.asList()
                .forEach { gameClient.addTeam(it).expectStatus().isCreated }
        }

        Then("the team is added") {
            response!!.expectStatus().isCreated
                .expectBody(JsonNode::class.java).consumeWith {
                    responseBody = it.responseBody!!
                    val teamId = responseBody.get("teamId")
                    assertThat(teamId).isNotNull()
                    scenario?.log("Team identifier $teamId")
                }
        }

        And("a token is returned to continue the game") {
            val accessToken = responseBody.get("accessToken").asText()
            assertThat(accessToken).isNotNull()
            scenario?.log("Team access token $accessToken")
        }

        And("the game starts automatically") {
            val eventType  = responseBody.get("eventType").asText()
            assertThat(eventType).isEqualTo("GameStarted")
        }

        And("a game started notification is sent") {
            sseResponseBody = gameClient.serverSentEventStream().responseBody.filter{ it.event() == "GameStarted" }
            StepVerifier.create(sseResponseBody!!)
                .assertNext { assertThat(it.event()).isEqualTo("GameStarted") }
                .thenCancel()
                .verify()
        }

        Then("the response contains a team name not available error") {
            response!!.expectStatus().isBadRequest
                .expectBody(JsonNode::class.java).consumeWith {
                    assertThat(
                        it.responseBody!!.get("message").asText()
                    ).isEqualTo("Team with name Locked and loaded already exists")
                }
        }

        Then("the response contains a team number limit exceeded error") {
            response!!.expectStatus().isBadRequest
                .expectBody(JsonNode::class.java).consumeWith {
                    assertThat(
                        it.responseBody!!.get("message").asText()
                    ).isEqualTo("Number of team limit reached")
                }
        }
    }

}