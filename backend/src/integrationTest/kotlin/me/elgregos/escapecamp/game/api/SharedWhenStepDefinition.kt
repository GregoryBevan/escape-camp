package me.elgregos.escapecamp.game.api

import io.cucumber.datatable.DataTable
import io.cucumber.java8.En
import org.springframework.beans.factory.annotation.Autowired


class SharedWhenStepDefinition : En {

    @Autowired
    private lateinit var gameClient: GameClient

    init {
        When("{int} contestants have been enrolled in the game") { _: Int, contestantNamesTable: DataTable ->
            contestantNamesTable.asList()
                .forEach { gameClient.enrollContestant(it).expectStatus().isCreated }
        }
    }
}
