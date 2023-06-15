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

val teamNames: List<String> = listOf("Locked and Loaded", "Jeepers Keypers", "The Escape Peas", "Sher-unlock")
var scenario: Scenario? = null
var organizerJwt: String? = null
var gameId: UUID? = null
var teams: MutableList<RegisteredTeam>? = null
var currentTeam: RegisteredTeam? = null
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

        Given("the {string} team registered for a game") { teamName: String ->
            createGame(gameClient)
            teams = mutableListOf()
            addAllTeams(gameClient)
            currentTeam = teams!!.first { it.name == teamName }
        }

        And("the game has started") {
            val filteredServerSentEventFlux = gameClient.serverSentEventStream().responseBody.filter { it.event() == "GameStarted" }
            StepVerifier.create(filteredServerSentEventFlux)
                .assertNext { assertThat(it.event()).isEqualTo("GameStarted") }
                .thenCancel()
                .verify()
        }

        And("the team has an assigned riddle") {
            assignNextTeamRiddle(gameClient)
        }

        And("the team has his last riddle assigned") {
            assignNextTeamRiddle(gameClient)
            solveTeamRiddle(gameClient, "riddle-4", "DDD")
            assignNextTeamRiddle(gameClient)
            solveTeamRiddle(gameClient, "riddle-1", "event sourcing")
            assignNextTeamRiddle(gameClient)
            solveTeamRiddle(gameClient, "riddle-2", "reactive")
            assignNextTeamRiddle(gameClient)
        }
    }

}

fun createGame(gameClient: GameClient) {
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

fun addAllTeams(gameClient: GameClient) {
    teamNames.forEach { teamName ->
        addTeam(gameClient, teamName)
    }
}

fun addTeam(gameClient: GameClient, teamName: String) {
    gameClient.addTeam(teamName)
        .expectStatus().isCreated
        .expectBody(JsonNode::class.java).consumeWith {
            val teamId = it.responseBody!!.get("teamId").asText()
            val accessToken = it.responseBody!!.get("accessToken").asText()
            assertThat(teamId).isNotNull()
            assertThat(accessToken).isNotNull()
            teams!!.add(RegisteredTeam(UUID.fromString(teamId), teamName, accessToken, teams!!.size + 1))
            scenario?.log("Team $teamName with identifier $teamId is added")
        }
}

fun assignNextTeamRiddle(gameClient: GameClient) {
    gameClient.requestNextRiddle(currentTeam!!)
        .expectStatus().isOk
        .expectBody(JsonNode::class.java).consumeWith {
            val riddle = genericObjectMapper.readValue<AssignedRiddle>(it.responseBody!!.get("riddle").toString())
            scenario?.log(
                """
                    Next riddle for ${currentTeam!!.name}:
                    $riddle
                    """
            )
        }
}

fun solveTeamRiddle(gameClient: GameClient, riddleName: String, solution: String) {
    gameClient.checkRiddleSolution(currentTeam!!, riddleName, solution)
        .expectStatus().isOk
}

data class RegisteredTeam(val id: UUID, val name: String, val accessToken: String, val order: Int)

data class AssignedRiddle(val name: String, val content: String)
