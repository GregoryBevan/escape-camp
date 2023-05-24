package me.elgregos.escapecamp.features

import com.fasterxml.jackson.databind.JsonNode
import io.cucumber.java8.En
import io.cucumber.java8.Scenario
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

var scenario: Scenario? = null
var organizerJwt: String? = null

class SharedGivenStepDefinition : En {

    @Autowired
    private lateinit var webTestClient: WebTestClient


    private val loginPayload = """
                        {
                            "username": "organizer",
                            "password": "7P2byKz39G!FGY"
                        }
                    """.trimIndent()

    init {
        Before { s: Scenario ->
            scenario = s
        }

        Given("an authenticated organizer") {
            webTestClient.post()
                .uri("/api/tokens")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(loginPayload))
                .exchange()
                .expectStatus().isOk
                .expectBody(JsonNode::class.java).consumeWith{
                    organizerJwt = it.responseBody?.get("accessToken")?.asText()
                }
        }
    }
}
