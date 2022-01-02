package org.codingforanimals.veganacademy.server.utils

import com.google.gson.Gson
import io.ktor.application.ApplicationCall
import io.ktor.request.receive
import org.codingforanimals.veganacademy.server.features.routes.common.Request

class RoutingUtils(private val gson: Gson) {

    suspend inline fun <reified T> receiveRequest(call: ApplicationCall): Request<T> {
        val type = genericType<Request<T>>()
        val requestRaw = call.receive<String>()
        return Gson().fromJson(requestRaw, type)
    }

}