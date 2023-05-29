package me.elgregos.escapecamp.game.api

import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.escapecamp.features.organizerJwt
import me.elgregos.reakteves.libs.genericObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import java.util.*

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
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(loginPayload))
        .exchange()
        .expectStatus().isOk
        .expectBody(JsonNode::class.java).consumeWith {
            organizerJwt = it.responseBody?.get("accessToken")?.asText()
        }

    fun createGame() =
        webTestClient.post()
            .uri(rootPath)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $organizerJwt")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()

    fun addTeam(gameId: UUID, teamName: String) =
        webTestClient.post()
            .uri { it.path(rootPath).pathSegment("$gameId").pathSegment("teams").build() }
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(genericObjectMapper.createObjectNode().put("name", teamName)))
            .exchange()
}