package me.elgregos.escapecamp.features

import io.cucumber.plugin.ConcurrentEventListener
import io.cucumber.plugin.event.EventHandler
import io.cucumber.plugin.event.EventPublisher
import io.cucumber.plugin.event.TestRunFinished
import io.cucumber.plugin.event.TestRunStarted
import me.elgregos.escapecamp.utils.extensions.postgres.PostgreSQLContainerHandler

class FeatureEventListener : ConcurrentEventListener {

    private val postgreSQLContainerHandler = PostgreSQLContainerHandler()

    override fun setEventPublisher(eventPublisher: EventPublisher) {
        eventPublisher.registerHandlerFor(TestRunStarted::class.java, setup)
        eventPublisher.registerHandlerFor(TestRunFinished::class.java, teardown)
    }

    private val setup = EventHandler<TestRunStarted> { beforeAll() }

    private fun beforeAll() {
        postgreSQLContainerHandler.start()
    }

    private val teardown = EventHandler<TestRunFinished> { afterAll() }

    private fun afterAll() {
        postgreSQLContainerHandler.stop()
    }
}

