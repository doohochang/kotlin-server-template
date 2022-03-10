package io.github.doohochang.ktserver.http

import io.github.doohochang.ktserver.json.PatchUserRequest
import io.github.doohochang.ktserver.json.PostUserRequest
import io.github.doohochang.ktserver.json.toJson
import io.github.doohochang.ktserver.service.UserService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.installUserApi(userService: UserService) = routing {
    get("/users/{id}") {
        val id = call.parameters["id"]!!
        userService.get(id).fold(
            ifRight = {
                if (it != null) call.respond(it.toJson())
                else call.respond(HttpStatusCode.NotFound)
            },
            ifLeft = {
                call.respond(HttpStatusCode.InternalServerError)
                log.error(it.transactionFailure)
            }
        )
    }

    post("/users") {
        val request = call.receive<PostUserRequest>()
        userService.create(request.name).fold(
            ifRight = {
                call.respond(it.toJson())
            },
            ifLeft = {
                when (it) {
                    is UserService.Dto.CreateFailure.InvalidName ->
                        call.respond(HttpStatusCode.BadRequest, "User name must be alphanumeric.")
                    is UserService.Dto.CreateFailure.TransactionFailed -> {
                        call.respond(HttpStatusCode.InternalServerError)
                        log.error(it.failure)
                    }
                }
            }
        )
    }

    patch("/users/{id}") {
        val id = call.parameters["id"]!!
        val request = call.receive<PatchUserRequest>()
        userService.update(id, request.name).fold(
            ifRight = {
                call.respond(it.toJson())
            },
            ifLeft = {
                when (it) {
                    is UserService.Dto.UpdateFailure.UserDoesNotExist ->
                        call.respond(HttpStatusCode.NotFound)
                    is UserService.Dto.UpdateFailure.InvalidName ->
                        call.respond(HttpStatusCode.BadRequest, "User name must be alphanumeric.")
                    is UserService.Dto.UpdateFailure.TransactionFailed -> {
                        call.respond(HttpStatusCode.InternalServerError)
                        log.error(it.failure)
                    }
                }
            }
        )
    }

    delete("/users/{id}") {
        val id = call.parameters["id"]!!
        userService.delete(id).fold(
            ifRight = {
                call.respond(HttpStatusCode.OK)
            },
            ifLeft = {
                when (it) {
                    is UserService.Dto.DeleteFailure.UserDoesNotExist ->
                        call.respond(HttpStatusCode.NotFound)
                    is UserService.Dto.DeleteFailure.TransactionFailed -> {
                        call.respond(HttpStatusCode.InternalServerError)
                        log.error(it.failure)
                    }
                }
            }
        )
    }
}
