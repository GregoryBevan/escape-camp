package me.elgregos.escapecamp.game.api

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.readValue
import io.cucumber.datatable.DataTable
import io.cucumber.java8.En
import io.cucumber.java8.Scenario
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
            contestants = mutableListOf()
        }

        Given("an authenticated organizer") {
            gameClient.authenticateOrganizer()
        }

        Given("an unauthenticated user") {
            organizerJwt = null
        }

        Given("a game with enrollment type {string} created with identifier") { enrollmentType: String ->
            createGame(gameClient, enrollmentType) }

        Given("the {string} contestant registered for a game") { contestantName: String ->
            createGame(gameClient)
            enrollAllContestants(gameClient)
            currentContestant = contestants!!.first { it.name == contestantName }
        }

        And("an unknown game identifier") {
            gameId = UUID.randomUUID()
            assertThat(gameId).isNotNull()
            scenario?.log("Unknown ame identifier $gameId")
        }

        And("{int} contestants have been enrolled in the game") { _: Int, contestantNamesTable: DataTable ->
            contestantNamesTable.asList()
                .forEach { enrollContestant(gameClient, it) }
        }

        And("the first riddle has been unlocked") {
            gameClient.unlockNextRiddle().expectStatus().isOk
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

        And("the {string} submit correct solution to the riddle {int}") { contestantName: String, riddleNumber: Int ->
            val contestant = contestants!!.first { contestant -> contestant.name == contestantName }
            assignContestantNextRiddle(gameClient, contestant)
            Thread.sleep(1000)
            solveContestantRiddle(gameClient, "riddle-$riddleNumber", "solution-$riddleNumber", contestant)
        }

        And("the contestant has his last riddle assigned") {
            assignContestantNextRiddle(gameClient)
            solveContestantRiddle(gameClient, "riddle-4", "solution-4")
            assignContestantNextRiddle(gameClient)
            solveContestantRiddle(gameClient, "riddle-1", "solution-1")
            assignContestantNextRiddle(gameClient)
            solveContestantRiddle(gameClient, "riddle-2", "solution-2")
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

fun assignContestantNextRiddle(gameClient: GameClient, contestant: RegisteredContestant? = currentContestant) {
    gameClient.requestNextRiddle(contestant!!)
        .expectStatus().isOk
        .expectBody(JsonNode::class.java).consumeWith {
            val riddle = genericObjectMapper.readValue<AssignedRiddle>(it.responseBody!!.get("riddle").toString())
            scenario?.log(
                """
                    Next riddle for ${contestant.name}:
                    $riddle
                    """
            )
        }
}

fun solveContestantRiddle(gameClient: GameClient, riddleName: String, solution: String, contestant: RegisteredContestant? = currentContestant) {
    gameClient.checkRiddleSolution(contestant!!, riddleName, solution)
        .expectStatus().isOk
}

data class RegisteredContestant(val id: UUID, val name: String, val accessToken: String, val order: Int)

data class AssignedRiddle(val name: String, val content: String)
