package me.elgregos.escapecamp.game.api

import io.cucumber.java8.En
import me.elgregos.escapecamp.features.currentTeam
import me.elgregos.escapecamp.features.response
import org.springframework.beans.factory.annotation.Autowired

class CheckRiddleSolutionApiStepDefinition: En {

    @Autowired
    private lateinit var gameClient: GameClient

    init {
        When("the team submit correct solution to the riddle") {
            response = gameClient.checkRiddleSolution(currentTeam!!, "riddle-3", "solution-3")
        }

        Then("the riddle is solved") {
            response!!.expectStatus().isOk
        }
    }
}