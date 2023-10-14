package me.elgregos.escapecamp.features

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.readValue
import io.cucumber.java8.En
import io.cucumber.java8.Scenario
import me.elgregos.escapecamp.game.api.GameClient
import me.elgregos.reakteves.libs.genericObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.test.StepVerifier
import java.util.*

val contestantNames: List<String> = listOf("Locked and Loaded", "Jeepers Keypers", "The Escape Peas", "Sher-unlock")
var scenario: Scenario? = null
var organizerJwt: String? = null
var gameId: UUID? = null
var contestants: MutableList<RegisteredContestant>? = null
var currentContestant: RegisteredContestant? = null
var response: WebTestClient.ResponseSpec? = null

class SharedGivenStepDefinition : En {

    @Autowired
    private lateinit var gameClient: GameClient

    init {
        Before { s: Scenario ->
            scenario = s
        }

        Given("an authenticated organizer") {
            gameClient.authenticateOrganizer()
        }

        Given("an unauthenticated user") {
            organizerJwt = null
        }

        Given("the {string} contestant registered for a game") { contestantName: String ->
            createGame(gameClient)
            contestants = mutableListOf()
            enrollAllContestants(gameClient)
            currentContestant = contestants!!.first { it.name == contestantName }
        }

        And("the game has started") {
            val filteredServerSentEventFlux = gameClient.serverSentEventStream().responseBody.filter { it.event() == "GameStarted" }
            StepVerifier.create(filteredServerSentEventFlux)
                .assertNext { assertThat(it.event()).isEqualTo("GameStarted") }
                .thenCancel()
                .verify()
        }

        And("the contestant has an assigned riddle") {
            assignContestantNextRiddle(gameClient)
        }

        And("the contestant has his last riddle assigned") {
            assignContestantNextRiddle(gameClient)
            solveContestantRiddle(gameClient, "riddle-4", "DDD")
            assignContestantNextRiddle(gameClient)
            solveContestantRiddle(gameClient, "riddle-1", "event sourcing")
            assignContestantNextRiddle(gameClient)
            solveContestantRiddle(gameClient, "riddle-2", "reactive")
            assignContestantNextRiddle(gameClient)
        }
    }

}

fun createGame(gameClient: GameClient, enrollmentType: String? = null) {
    gameClient.authenticateOrganizer()
    gameClient.createGame(enrollmentType)
        .expectStatus().isCreated
        .expectBody(JsonNode::class.java).consumeWith {
            gameId = UUID.fromString(
                it.responseBody?.get("gameId")?.asText() ?: throw Exception("Error while getting game id")
            )
            assertThat(gameId).isNotNull()
            scenario?.log("Game identifier $gameId")
        }
}

fun enrollAllContestants(gameClient: GameClient) {
    contestantNames.forEach { contestantName ->
        enrollContestant(gameClient, contestantName)
    }
}

fun enrollContestant(gameClient: GameClient, contestantName: String) {
    gameClient.enrollContestant(contestantName)
        .expectStatus().isCreated
        .expectBody(JsonNode::class.java).consumeWith {
            val contestantId = it.responseBody!!.get("contestantId").asText()
            val accessToken = it.responseBody!!.get("accessToken").asText()
            assertThat(contestantId).isNotNull()
            assertThat(accessToken).isNotNull()
            contestants!!.add(RegisteredContestant(UUID.fromString(contestantId), contestantName, accessToken, contestants!!.size + 1))
            scenario?.log("Contestant $contestantName with identifier $contestantId is enrolled")
        }
}

fun assignContestantNextRiddle(gameClient: GameClient) {
    gameClient.requestNextRiddle(currentContestant!!)
        .expectStatus().isOk
        .expectBody(JsonNode::class.java).consumeWith {
            val riddle = genericObjectMapper.readValue<AssignedRiddle>(it.responseBody!!.get("riddle").toString())
            scenario?.log(
                """
                    Next riddle for ${currentContestant!!.name}:
                    $riddle
                    """
            )
        }
}

fun solveContestantRiddle(gameClient: GameClient, riddleName: String, solution: String) {
    gameClient.checkRiddleSolution(currentContestant!!, riddleName, solution)
        .expectStatus().isOk
}

data class RegisteredContestant(val id: UUID, val name: String, val accessToken: String, val order: Int)

data class AssignedRiddle(val name: String, val content: String)
