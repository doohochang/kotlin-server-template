package io.github.doohochang.ktserver.http

import io.ktor.server.engine.*
import io.ktor.server.netty.*

object Server {
    fun start(port: Int) {
        embeddedServer(Netty, port = port) {
            greetingModule()
        }.start(wait = false)
    }
}
