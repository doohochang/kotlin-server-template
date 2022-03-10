package io.github.doohochang.ktserver.http

import io.github.doohochang.ktserver.service.UserService
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

class Server(
    private val httpConfiguration: HttpConfiguration,
    private val userService: UserService
) {
    fun start() {
        embeddedServer(Netty, port = httpConfiguration.port) {
            install(ContentNegotiation) { json() }
            installCallLogging()

            installGreetingApi()
            installUserApi(userService)
        }.start(wait = false)
    }
}
