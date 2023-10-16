package me.elgregos.escapecamp.game.api

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.cucumber.java8.En
import org.springframework.beans.factory.annotation.Autowired
import reactor.test.StepVerifier

class CheckRiddleSolutionApiStepDefinition : En {

    @Autowired
    private lateinit var gameClient: GameClient

    init {
        When("the contestant submit correct solution to the riddle") {
            response = gameClient.checkRiddleSolution(currentContestant!!, "riddle-3", "solution-3")
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
