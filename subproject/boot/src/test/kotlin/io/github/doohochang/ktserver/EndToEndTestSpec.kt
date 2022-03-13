package io.github.doohochang.ktserver

import io.github.doohochang.ktserver.json.PatchUserRequest
import io.github.doohochang.ktserver.json.PostUserRequest
import io.github.doohochang.ktserver.json.User
import io.github.doohochang.ktserver.repository.Postgresql
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

/**
 * Contains end-to-end tests for the application, especially for the server APIs.
 * It actually boots the entire application including necessary external dependencies such as a database, and then tests it.
 */
class EndToEndTestSpec : FreeSpec({
    "End-To-End Test" - {
        val originalConfiguration = Configuration.load()

        val postgresql = Postgresql(
            database = originalConfiguration.postgresql.database,
            username = originalConfiguration.postgresql.username,
            password = originalConfiguration.postgresql.password
        )

        val postgresqlPort = postgresql.startAndInitialize()

        val configuration = originalConfiguration.copy(
            postgresql = originalConfiguration.postgresql.copy(port = postgresqlPort)
        )

        val infrastructure = Infrastructure.load(configuration)
        val domain = Domain.load(infrastructure)
        val presentation = Presentation.load(configuration, domain)

        "Test HttpServer" - {
            val client = HttpClient(CIO) {
                install(JsonFeature)
                defaultRequest {
                    host = "localhost"
                    port = configuration.http.port
                }
                expectSuccess = false
            }

            presentation.httpServer.start()

            "Greeting APIs" {
                client.get<String>(path = "/greeting/alice") shouldBe "Hello, alice.\n"
            }

            "User APIs" {
                // Tests POST /users from here.
                val user1 = client.post<User>(path = "/users") {
                    contentType(ContentType.Application.Json)
                    body = PostUserRequest("alice")
                }
                user1.name shouldBe "alice"

                val user2 = client.post<User>(path = "/users") {
                    contentType(ContentType.Application.Json)
                    body = PostUserRequest("bob")
                }
                user2.name shouldBe "bob"

                client.post<HttpStatement>(path = "/users") {
                    contentType(ContentType.Application.Json)
                    body = PostUserRequest("!@#")
                }.execute().status shouldBe HttpStatusCode.BadRequest

                // Tests GET /users/{id} from here.
                client.get<User>(path = "/users/${user1.id}") shouldBe user1
                client.get<User>(path = "/users/${user2.id}") shouldBe user2

                client.get<HttpStatement>(path = "/users/non-existing-user-id")
                    .execute()
                    .status shouldBe HttpStatusCode.NotFound

                // Tests PATCH /users/{id} from here.
                val updatedUser1 = client.patch<User>(path = "/users/${user1.id}") {
                    contentType(ContentType.Application.Json)
                    body = PatchUserRequest("charlie")
                }
                updatedUser1 shouldBe User(user1.id, "charlie")
                client.get<User>(path = "/users/${user1.id}") shouldBe updatedUser1

                val updatedUser2 = client.patch<User>(path = "/users/${user2.id}") {
                    contentType(ContentType.Application.Json)
                    body = PatchUserRequest("damien")
                }
                updatedUser2 shouldBe User(user2.id, "damien")
                client.get<User>(path = "/users/${user2.id}") shouldBe updatedUser2

                client.patch<HttpStatement>(path = "/users/${user1.id}") {
                    contentType(ContentType.Application.Json)
                    body = PatchUserRequest("!@#$%")
                }.execute().status shouldBe HttpStatusCode.BadRequest

                client.patch<HttpStatement>(path = "/users/non-existing-user-id") {
                    contentType(ContentType.Application.Json)
                    body = PatchUserRequest("eve")
                }.execute().status shouldBe HttpStatusCode.NotFound

                // Tests DELETE /users/{id} from here.
                client.delete<HttpStatement>(path = "/users/${user1.id}")
                    .execute().status shouldBe HttpStatusCode.OK
                client.get<HttpStatement>(path = "/users/${user1.id}")
                    .execute().status shouldBe HttpStatusCode.NotFound

                client.delete<HttpStatement>(path = "/users/${user2.id}")
                    .execute().status shouldBe HttpStatusCode.OK
                client.get<HttpStatement>(path = "/users/${user2.id}")
                    .execute().status shouldBe HttpStatusCode.NotFound

                client.delete<HttpStatement>(path = "/users/${user1.id}")
                    .execute().status shouldBe HttpStatusCode.NotFound
                client.delete<HttpStatement>(path = "/users/${user2.id}")
                    .execute().status shouldBe HttpStatusCode.NotFound
            }

            client.close()
            presentation.httpServer.stop()
        }

        postgresql.stop()
    }
})
