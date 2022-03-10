package io.github.doohochang.ktserver

import arrow.core.Either
import arrow.core.computations.either
import com.typesafe.config.ConfigFactory
import io.github.doohochang.ktserver.configuration.PostgresqlConfiguration
import io.github.doohochang.ktserver.http.HttpConfiguration
import io.github.doohochang.ktserver.http.Server
import io.github.doohochang.ktserver.repository.PostgresqlConnectionPool
import io.github.doohochang.ktserver.repository.UserRepositoryImpl
import io.github.doohochang.ktserver.service.UserService
import org.slf4j.LoggerFactory

suspend fun main() {
    val bootResult = either<Throwable, Unit> {
        // Dependency injection from here.
        val rootConfiguration = ConfigFactory.load()
        val httpConfiguration = HttpConfiguration.from(rootConfiguration).bind()
        val postgresqlConfiguration = PostgresqlConfiguration.from(rootConfiguration).bind()

        val postgresqlConnectionPool = PostgresqlConnectionPool(postgresqlConfiguration)
        val userRepository = UserRepositoryImpl(postgresqlConnectionPool)

        val userService = UserService(userRepository)

        val httpServer = Server(httpConfiguration, userService)

        // Starts the server.
        httpServer.start()
    }

    val log = LoggerFactory.getLogger("io.github.doohochang.ktserver.MainKt")

    when (bootResult) {
        is Either.Right -> log.info("Server has been started successfully.")
        is Either.Left -> log.error(bootResult.value.stackTraceToString())
    }
}
