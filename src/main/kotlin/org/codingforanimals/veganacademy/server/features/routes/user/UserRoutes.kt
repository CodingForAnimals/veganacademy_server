package org.codingforanimals.veganacademy.server.features.routes.user

import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.locations.KtorExperimentalLocationsAPI
import io.ktor.server.locations.get
import io.ktor.server.locations.post
import io.ktor.server.routing.Route
import io.ktor.server.sessions.clear
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import org.codingforanimals.veganacademy.server.config.plugins.UserSession
import org.codingforanimals.veganacademy.server.features.model.service.UserService
import org.codingforanimals.veganacademy.server.features.model.service.UserService.Companion.MESSAGE_LOGOUT_SUCCESS
import org.codingforanimals.veganacademy.server.utils.errorResponse
import org.codingforanimals.veganacademy.server.utils.getRequest
import org.codingforanimals.veganacademy.server.utils.successResponse
import org.koin.ktor.ext.inject

@KtorExperimentalLocationsAPI
fun Route.userRoutes() {
    val userService by inject<UserService>()

    post<UserLocations.Register> {
        try {
            val request = call.getRequest<UserRegisterRequest>().content
            val response = userService.register(request)
            response.content?.let { call.setUserSession(it.user.userId) }
            call.successResponse(response)
        } catch (e: Throwable) {
            call.errorResponse(e)
        }
    }

    post<UserLocations.Login> {
        try {
            val request = call.getRequest<UserLoginRequest>()
            val response = userService.login(request.content)
            response.content?.let { call.setUserSession(it.user.userId) }
            call.successResponse(response)
        } catch (e: Throwable) {
            call.errorResponse(e)
        }
    }

    get<UserLocations.Logout> {
        try {
            call.clearUserSession()
            call.successResponse(MESSAGE_LOGOUT_SUCCESS)
        } catch (e: Throwable) {
            call.errorResponse(e)
        }
    }
}

private fun ApplicationCall.setUserSession(userId: Int) {
    sessions.set(UserSession(userId))
}

private fun ApplicationCall.clearUserSession() {
    sessions.clear<UserSession>()
}