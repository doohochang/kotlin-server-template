package io.github.doohochang.ktserver.http

import io.github.doohochang.ktserver.service.UserService
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.util.concurrent.TimeUnit

class HttpServer(
    private val httpConfiguration: HttpConfiguration,
    private val userService: UserService
) {
    private val embeddedServer by lazy {
        embeddedServer(Netty, port = httpConfiguration.port) {
            install(ContentNegotiation) { json() }
            installCallLogging()

            installGreetingApi()
            installUserApi(userService)
        }
    }

    fun start() {
        embeddedServer.start(wait = false)
    }

    fun stop() {
        embeddedServer.stop(1, 3, TimeUnit.SECONDS)
    }
}
