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

class EnrollContestantApiStepDefinition : En {

    @Autowired
    private lateinit var gameClient: GameClient

    private lateinit var responseBody: JsonNode

    init {

        Given("a contestant with game identifier") {
            createGame(gameClient)
        }

        Given("a contestant with an unknown game identifier") {
            gameId = UUID.randomUUID()
            assertThat(gameId).isNotNull()
            scenario?.log("Unknown ame identifier $gameId")
        }

        And("a contestant with name {string} has been enrolled to the game") { contestantName: String ->
            gameClient.enrollContestant(contestantName)
                .expectBody(JsonNode::class.java).consumeWith {
                    responseBody = it.responseBody!!
                    assertThat(responseBody.get("contestantId")).isNotNull()
                }
        }

        When("he enrolls in the game with name {string}") { contestantName: String ->
            response = gameClient.enrollContestant(contestantName)
        }

        When("{int} contestants have been enrolled in the game") { _: Int, contestantNamesTable: DataTable ->
            contestantNamesTable.asList()
                .forEach { gameClient.enrollContestant(it).expectStatus().isCreated }
        }

        Then("the contestant is enrolled") {
            response!!.expectStatus().isCreated
                .expectBody(JsonNode::class.java).consumeWith {
                    responseBody = it.responseBody!!
                    val contestantId = responseBody.get("contestantId")
                    assertThat(contestantId).isNotNull()
                    scenario?.log("Contestant identifier $contestantId")
                }
        }

        And("a token is returned to continue the game") {
            val accessToken = responseBody.get("accessToken").asText()
            assertThat(accessToken).isNotNull()
            scenario?.log("Contestant access token $accessToken")
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

        Then("the response contains a contestant name not available error") {
            response!!.expectStatus().isBadRequest
                .expectBody(JsonNode::class.java).consumeWith {
                    assertThat(
                        it.responseBody!!.get("message").asText()
                    ).isEqualTo("Contestant with name Locked and loaded already exists")
                }
        }

        Then("the response contains a contestant number limit exceeded error") {
            response!!.expectStatus().isBadRequest
                .expectBody(JsonNode::class.java).consumeWith {
                    assertThat(
                        it.responseBody!!.get("message").asText()
                    ).isEqualTo("Number of contestant limit reached")
                }
        }
    }

}
