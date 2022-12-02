package org.codingforanimals.veganacademy.server.features.routes.user.rememberme

import io.ktor.server.application.call
import io.ktor.server.locations.KtorExperimentalLocationsAPI
import io.ktor.server.locations.Location
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.codingforanimals.veganacademy.server.features.model.data.dto.RememberMeCredentialsDTO
import org.codingforanimals.veganacademy.server.features.model.service.RememberMeService
import org.codingforanimals.veganacademy.server.utils.errorResponse
import org.codingforanimals.veganacademy.server.utils.getRequest
import org.koin.ktor.ext.inject

@OptIn(KtorExperimentalLocationsAPI::class)
@Location("/remember-me")
class RememberMeLocations

fun Route.rememberMeRoutes() {

    val rememberMeService: RememberMeService by inject()

    post<RememberMeLocations> {
        try {
            val request = call.getRequest<RememberMeCredentialsDTO>()
            val response = rememberMeService.validateRememberMeCredentials(request.content)
            call.respond(response)
        } catch (e: Throwable) {
            call.errorResponse(e)
        }
    }
}