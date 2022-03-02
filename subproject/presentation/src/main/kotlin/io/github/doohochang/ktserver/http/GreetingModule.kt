package io.github.doohochang.ktserver.http

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.greetingModule() {
    routing {
        get("/greeting/{name}") {
            val name = call.parameters["name"]

            if (name == null) call.respond(HttpStatusCode.InternalServerError)
            else call.respondText("Hello, $name.\n")
        }
    }
}
