package io.github.doohochang.ktserver.http

import io.ktor.application.*
import io.ktor.features.*
import org.slf4j.event.Level
import java.util.UUID

fun Application.installCallLogging() {
    install(CallLogging) {
        level = Level.INFO
        mdc("traceId") { it.request.headers["X-B3-TraceId"] }
        mdc("requestId") { UUID.randomUUID().toString() }
    }
}
