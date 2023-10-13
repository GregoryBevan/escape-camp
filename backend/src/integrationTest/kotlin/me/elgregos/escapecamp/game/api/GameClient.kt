package me.elgregos.escapecamp.game.api

import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.escapecamp.features.RegisteredContestant
import me.elgregos.escapecamp.features.gameId
import me.elgregos.escapecamp.features.organizerJwt
import me.elgregos.reakteves.libs.genericObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.*
import org.springframework.http.codec.ServerSentEvent
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import org.springframework.web.reactive.function.BodyInserters

class GameClient(private val webTestClient: WebTestClient) {

    private val rootPath = "/api/games"

    private val loginPayload = """
                        {
                            "username": "organizer",
                            "password": "7P2byKz39G!FGY"
                        }
                    """.trimIndent()

    fun authenticateOrganizer() = webTestClient.post()
        .uri("/api/tokens")
        .accept(APPLICATION_JSON)
        .contentType(APPLICATION_JSON)
        .body(BodyInserters.fromValue(loginPayload))
        .exchange()
        .expectStatus().isOk
        .expectBody(JsonNode::class.java).consumeWith {
            organizerJwt = it.responseBody?.get("accessToken")?.asText()
        }



    fun listGame() =
        webTestClient.get()
            .uri(rootPath)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $organizerJwt")
            .accept(APPLICATION_JSON)
            .exchange()

    fun createGame() =
        webTestClient.post()
            .uri(rootPath)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $organizerJwt")
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .accept(APPLICATION_JSON)
            .exchange()

    fun addContestant(contestantName: String) =
        webTestClient.post()
            .uri { it.path(rootPath).pathSegment("$gameId", "contestants").build() }
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .accept(APPLICATION_JSON)
            .body(BodyInserters.fromValue(genericObjectMapper.createObjectNode().put("name", contestantName)))
            .exchange()

    fun requestNextRiddle(contestant: RegisteredContestant) =
        webTestClient.get()
            .uri { it.path(rootPath).pathSegment("$gameId", "contestants", "${contestant.id}", "riddle").build() }
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${contestant.accessToken}")
            .accept(APPLICATION_JSON)
            .exchange()

    fun checkRiddleSolution(contestant: RegisteredContestant, riddleName: String, solution: String) =
        webTestClient.post()
            .uri { it.path(rootPath).pathSegment("$gameId", "contestants", "${contestant.id}", "riddle", riddleName).build() }
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${contestant.accessToken}")
            .accept(APPLICATION_JSON)
            .body(BodyInserters.fromValue(genericObjectMapper.createObjectNode().put("solution", solution)))
            .exchange()

    fun serverSentEventStream() =
        webTestClient.get()
            .uri { it.path(rootPath).pathSegment("$gameId", "events-stream").build() }
            .header(HttpHeaders.AUTHORIZATION, "Bearer $organizerJwt")
            .accept(TEXT_EVENT_STREAM)
            .exchange()
            .expectStatus().isOk()
            .returnResult<ServerSentEvent<JsonNode>>()
}
