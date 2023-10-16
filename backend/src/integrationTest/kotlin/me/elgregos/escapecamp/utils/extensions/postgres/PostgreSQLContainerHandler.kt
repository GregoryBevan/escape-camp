package me.elgregos.escapecamp.utils.extensions.postgres

import io.github.oshai.kotlinlogging.KotlinLogging
import org.rnorth.ducttape.unreliables.Unreliables
import org.testcontainers.containers.ContainerLaunchException
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.AbstractWaitStrategy
import org.testcontainers.delegate.AbstractDatabaseDelegate
import org.testcontainers.ext.ScriptUtils
import java.sql.Connection
import java.sql.DriverManager
import java.util.*
import java.util.concurrent.TimeUnit

private val logger = KotlinLogging.logger {}

class PostgreSQLContainerHandler {

    companion object {
        val postgreSQLContainer: PostgreSQLContainer<Nothing> = PostgreSQLContainer<Nothing>("postgres:16")
            .waitingFor(PostgresSQLWaitStrategy())
    }
    fun start() {
        postgreSQLContainer.start()
        registerSpringProperties()
    }

    fun stop() {
        postgreSQLContainer.stop()
    }

    private fun registerSpringProperties() {
        System.setProperty("spring.r2dbc.url", postgreSQLContainer.jdbcUrl.replace("jdbc", "r2dbc"))
        System.setProperty("spring.r2dbc.username", postgreSQLContainer.username)
        System.setProperty("spring.r2dbc.password", postgreSQLContainer.password)
        System.setProperty("spring.liquibase.change-log", "classpath:db/changelog/db-changelog.sql")
        System.setProperty("spring.liquibase.url", postgreSQLContainer.jdbcUrl)
        System.setProperty("spring.liquibase.user", postgreSQLContainer.username)
        System.setProperty("spring.liquibase.password", postgreSQLContainer.password)
        System.setProperty("database.url", postgreSQLContainer.jdbcUrl)
        System.setProperty("database.username", postgreSQLContainer.username)
        System.setProperty("database.password", postgreSQLContainer.password)
    }

    class PostgresSQLWaitStrategy : AbstractWaitStrategy() {

        override fun waitUntilReady() {
            try {
                Unreliables.retryUntilSuccess(startupTimeout.seconds.toInt(), TimeUnit.SECONDS) {
                    rateLimiter.doWhenReady {
                        TestContainerPostgresSQLDelegate().execute(
                            "select 555", "", 1,
                            continueOnError = false,
                            ignoreFailedDrops = false
                        )
                    }
                }
            } catch (e: Exception) {
                throw ContainerLaunchException("Timed out waiting for PostgresSQL to be accessible for query execution", e)
            }
        }
    }

    class TestContainerPostgresSQLDelegate : AbstractDatabaseDelegate<Connection>() {

        override fun createNewConnection() =
            DriverManager.getConnection(
                postgreSQLContainer.jdbcUrl,
                Properties().also {
                    it["user"] = postgreSQLContainer.username
                    it["password"] = postgreSQLContainer.password
                })

        override fun execute(
            statement: String,
            scriptPath: String,
            lineNumber: Int,
            continueOnError: Boolean,
            ignoreFailedDrops: Boolean,
        ) {
            val result = getConnection().prepareStatement(statement).executeQuery()
            result.next()
            if (result.getInt(1) == (555)) {
                logger.debug { "Statement $statement was applied" }
            } else {
                throw ScriptUtils.ScriptStatementFailedException(statement, lineNumber, scriptPath)
            }
        }

        override fun closeConnectionQuietly(connection: Connection?) {
            connection?.close() ?: logger.error { "Unable to close connection" }
        }

    }
}
