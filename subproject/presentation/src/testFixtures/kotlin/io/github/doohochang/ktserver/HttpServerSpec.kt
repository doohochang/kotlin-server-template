package io.github.doohochang.ktserver

import io.github.doohochang.ktserver.http.HttpConfiguration
import io.github.doohochang.ktserver.http.HttpServer
import io.kotest.core.spec.style.scopes.FreeSpecContainerScope
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.request.*

suspend fun FreeSpecContainerScope.testHttpServer(httpServer: HttpServer, httpConfiguration: HttpConfiguration) {
    "Test HttpServer" - {
        val client: HttpClient = HttpClient(CIO) {
            defaultRequest {
                host = "localhost"
                port = httpConfiguration.port
            }
        }

        httpServer.start()

        "GET /greeting/{name}" {
            client.get<String>(path = "/greeting/alice") shouldBe "Hello, alice.\n"
        }

        // TODO: Write tests for User APIs

        client.close()
        httpServer.stop()
    }
}
