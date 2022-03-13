package io.github.doohochang.ktserver.repository

import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactoryOptions
import org.slf4j.LoggerFactory
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.await
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import java.lang.RuntimeException

class Postgresql(
    private val database: String,
    private val username: String,
    private val password: String
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    private val container = PostgreSQLContainer(POSTGRESQL_IMAGE_TAG)
        .withExposedPorts(POSTGRESQL_PORT)
        .withDatabaseName(database)
        .withUsername(username)
        .withPassword(password)
        .withLogConsumer(Slf4jLogConsumer(log))

    val port: Int
        get() =
            if (container.isRunning) container.getMappedPort(POSTGRESQL_PORT)
            else throw RuntimeException("The container is not running")

    /**
     * Starts the PostgreSQL container and initialize it with SQL in resource file whose path is [POSTGRESQL_INIT_SCRIPT_PATH].
     * This function also returns the mapped port of the container.
     */
    suspend fun startAndInitialize(): Int {
        container.start()

        val port = container.getMappedPort(POSTGRESQL_PORT)

        val connectionPool = ConnectionFactories.get(
            ConnectionFactoryOptions.builder()
                .option(ConnectionFactoryOptions.DRIVER, "pool")
                .option(ConnectionFactoryOptions.PROTOCOL, "postgresql")
                .option(ConnectionFactoryOptions.HOST, "localhost")
                .option(ConnectionFactoryOptions.PORT, port)
                .option(ConnectionFactoryOptions.USER, username)
                .option(ConnectionFactoryOptions.PASSWORD, password)
                .option(ConnectionFactoryOptions.DATABASE, database)
                .build()
        )

        val databaseClient = DatabaseClient.create(connectionPool)
        val initScript = this::class.java.classLoader.getResource(POSTGRESQL_INIT_SCRIPT_PATH)!!.readText()
        databaseClient.sql(initScript).await()

        return port
    }

    fun stop() {
        container.stop()
    }

    companion object {
        const val POSTGRESQL_IMAGE_TAG = "postgres:14.2"
        const val POSTGRESQL_INIT_SCRIPT_PATH = "init-test.sql"
        const val POSTGRESQL_PORT = 5432
    }
}
