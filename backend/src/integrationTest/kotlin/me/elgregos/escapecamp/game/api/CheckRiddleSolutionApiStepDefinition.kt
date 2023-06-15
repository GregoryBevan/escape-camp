package me.elgregos.escapecamp.game.api

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.cucumber.java8.En
import me.elgregos.escapecamp.features.currentTeam
import me.elgregos.escapecamp.features.response
import me.elgregos.escapecamp.features.sseResponseBody
import org.springframework.beans.factory.annotation.Autowired
import reactor.test.StepVerifier

class CheckRiddleSolutionApiStepDefinition : En {

    @Autowired
    private lateinit var gameClient: GameClient

    init {
        When("the team submit correct solution to the riddle") {
            response = gameClient.checkRiddleSolution(currentTeam!!, "riddle-3", "craft")
        }

        Then("the riddle is solved") {
            response!!.expectStatus().isOk
        }

        And("a winner announced notification is sent") {
            sseResponseBody = gameClient.serverSentEventStream().responseBody.filter{ it.event() == "WinnerAnnounced" }
            StepVerifier.create(sseResponseBody!!)
                .assertNext { assertThat(it.event()).isEqualTo("WinnerAnnounced") }
                .thenCancel()
                .verify()
        }
    }
}
