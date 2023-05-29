package me.elgregos.escapecamp.features

import io.cucumber.java8.En


class SharedThenStepDefinition : En {

    init {

        Then("an authentication error is returned") {
            response!!.expectStatus()
                .isUnauthorized()
        }
    }
}
