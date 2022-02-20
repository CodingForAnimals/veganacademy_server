package org.codingforanimals.veganacademy.server.features.routes.user.rememberme

import io.ktor.application.call
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.response.respond
import io.ktor.routing.Route
import org.codingforanimals.veganacademy.server.features.model.data.dto.RememberMeCredentialsDTO
import org.codingforanimals.veganacademy.server.features.model.service.RememberMeService
import org.codingforanimals.veganacademy.server.utils.errorResponse
import org.codingforanimals.veganacademy.server.utils.getRequest
import org.koin.ktor.ext.inject

@KtorExperimentalLocationsAPI
@Location("/remember-me")
class RememberMeLocations

@KtorExperimentalLocationsAPI
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