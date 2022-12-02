package org.codingforanimals.veganacademy.server.features.routes.common

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

class Response<T> private constructor(
    var success: Boolean,
    var message: String?,
    var content: T?,
) {

    companion object {
        fun <T> success(message: String, content: T? = null): Response<T> {
            return Response(
                success = true,
                message = message,
                content = content,
            )
        }

        fun <T> failure(message: String, content: T? = null): Response<T> {
            return Response(
                success = false,
                message = message,
                content = content
            )
        }
    }
}

suspend fun respondWithFailure(message: String, call: ApplicationCall, content: Any? = null) {
    call.respond(HttpStatusCode.InternalServerError, Response.failure(message, content))
}