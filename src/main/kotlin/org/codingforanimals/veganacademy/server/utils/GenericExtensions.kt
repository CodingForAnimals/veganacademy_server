package org.codingforanimals.veganacademy.server.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.application.ApplicationCall
import io.ktor.application.log
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.request.uri
import io.ktor.response.respond
import org.codingforanimals.veganacademy.server.features.routes.common.Request
import org.codingforanimals.veganacademy.server.features.routes.common.Response
import org.koin.java.KoinJavaComponent.inject

val gson: Gson by inject(Gson::class.java)

private const val MESSAGE_SERVER_ERROR = "Unexpected internal server error"

suspend inline fun <reified T : Any> ApplicationCall.getRequest(): Request<T> {
    val requestType = object : TypeToken<Request<T>>() {}.type
    return gson.fromJson(receive<String>(), requestType) as Request<T>
}

suspend fun ApplicationCall.errorResponse(e: Throwable) {
    application.log.error("Error in route ${request.uri}", e)
    respond(HttpStatusCode.InternalServerError, Response.failure<String>(MESSAGE_SERVER_ERROR))
}

suspend fun <T> ApplicationCall.successResponse(response: Response<T>) {
    respond(HttpStatusCode.OK, response)
}

suspend fun ApplicationCall.successResponse(message: String) {
    respond(HttpStatusCode.OK, Response.success<String>(message))
}