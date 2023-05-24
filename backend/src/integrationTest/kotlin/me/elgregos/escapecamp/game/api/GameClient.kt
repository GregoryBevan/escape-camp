package me.elgregos.escapecamp.game.api

import me.elgregos.escapecamp.features.organizerJwt
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

class GameClient(private val webTestClient: WebTestClient) {

    private val rootPath = "/api/games"

    fun createGame(): WebTestClient.ResponseSpec =
        webTestClient.post()
            .uri(rootPath)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $organizerJwt")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
}