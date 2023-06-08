package me.elgregos.escapecamp.features

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.fasterxml.jackson.databind.JsonNode
import io.cucumber.java8.En
import io.cucumber.java8.Scenario
import me.elgregos.escapecamp.game.api.GameClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.test.StepVerifier
import java.util.*

val teamNames: List<String> = listOf("Locked and Loaded", "Jeepers Keypers", "The Escape Peas", "Sher-unlock")
var scenario: Scenario? = null
var organizerJwt: String? = null
var gameId: UUID? = null
val teams: MutableList<RegisteredTeam> = mutableListOf()
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
            addAllTeams(gameClient)
            currentTeam = teams.first { it.name == teamName }
        }

        And("the game has started") {
            StepVerifier.create(gameClient.serverSentEventStream(gameId!!).responseBody.filter { it.event() == "GameStarted"})
//                .assertNext { assertThat(it.event()).isEqualTo("GameCreated") }
//                .assertNext { assertThat(it.event()).isEqualTo("TeamAdded") }
//                .assertNext { assertThat(it.event()).isEqualTo("TeamAdded") }
//                .assertNext { assertThat(it.event()).isEqualTo("TeamAdded") }
                .assertNext { assertThat(it.event()).isEqualTo("TeamAdded") }
                .assertNext { assertThat(it.event()).isEqualTo("GameStarted") }
                .thenCancel()
                .verify()
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
    gameClient.addTeam(gameId!!, teamName)
        .expectStatus().isCreated
        .expectBody(JsonNode::class.java).consumeWith {
            val teamId = it.responseBody!!.get("teamId").asText()
            assertThat(teamId).isNotNull()
            teams.add(RegisteredTeam(UUID.fromString(teamId), teamName))
            scenario?.log("Team $teamName with identifier $teamId is added")
        }
}

data class RegisteredTeam(val id: UUID, val name: String)
