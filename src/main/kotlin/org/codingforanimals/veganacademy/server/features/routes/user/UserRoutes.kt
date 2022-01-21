package org.codingforanimals.veganacademy.server.features.routes.user

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.routing.Route
import io.ktor.sessions.clear
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import org.codingforanimals.veganacademy.server.config.plugins.UserSession
import org.codingforanimals.veganacademy.server.features.model.service.UserService
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

private const val MESSAGE_LOGOUT_SUCCESS = "User logout success"

private fun ApplicationCall.setUserSession(userId: Int) {
    sessions.set(UserSession(userId))
}

private fun ApplicationCall.clearUserSession() {
    sessions.clear<UserSession>()
}