package io.github.doohochang.ktserver

import arrow.core.Either
import arrow.core.computations.either
import com.typesafe.config.ConfigFactory
import io.github.doohochang.ktserver.http.HttpConfiguration
import io.github.doohochang.ktserver.http.Server

suspend fun main() {
    val result = either<Throwable, Unit> {
        val config = ConfigFactory.load()
        val httpConfiguration = HttpConfiguration.from(config).bind()

        Server.start(httpConfiguration.port)
    }

    when(result) {
        is Either.Right -> println("Server has been started successfully.")
        is Either.Left -> println(result.value)
    }
}
