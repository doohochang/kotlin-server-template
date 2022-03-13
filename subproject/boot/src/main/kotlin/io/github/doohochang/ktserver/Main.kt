package io.github.doohochang.ktserver

import org.slf4j.LoggerFactory

suspend fun main() {
    val log = LoggerFactory.getLogger("io.github.doohochang.ktserver.MainKt")

    try {
        val application = Application.load()

        // Starts the server.
        application.presentation.httpServer.start()
        log.info("Server has been started successfully.")
    } catch (e: Throwable) {
        log.error(e.stackTraceToString())
    }
}
