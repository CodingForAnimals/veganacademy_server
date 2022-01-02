package org.codingforanimals.veganacademy.server.features.routes.user

import io.ktor.application.application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.request.uri
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.sessions.clear
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import org.codingforanimals.veganacademy.server.config.plugins.AUTH_SESSION
import org.codingforanimals.veganacademy.server.config.plugins.UserSession
import org.codingforanimals.veganacademy.server.features.model.repository.UserRepository
import org.codingforanimals.veganacademy.server.features.model.repository.mapper.toDto
import org.codingforanimals.veganacademy.server.features.routes.common.Response
import org.codingforanimals.veganacademy.server.features.routes.common.respondWithFailure
import org.codingforanimals.veganacademy.server.utils.UserUtils
import org.koin.ktor.ext.inject

@KtorExperimentalLocationsAPI
fun Route.userRoutes() {

    val userUtils by inject<UserUtils>()
    val userRepository by inject<UserRepository>()

    authenticate(AUTH_SESSION) {
        get<UserLocations.GetAll> {
            try {
                val users = userRepository.findAllUsers()
                call.respond(Response.success("List of users fetched successfully", users))
            } catch (e: Throwable) {
                application.log.error("Failed to get all users", e)
                call.respond(HttpStatusCode.BadRequest, "Error getting users")
            }
        }
    }

    get<UserLocations.Delete> {
        try {
            val deleted = userRepository.deleteAll()
            call.respond(deleted)
        } catch (e: Throwable) {
            application.log.error("Error deleting users", e)
            call.respond(HttpStatusCode.InternalServerError, "Error deleting users")
        }
    }

    post<UserLocations.Register> {
        try {
            val body = call.receive<Parameters>()
            val email = body["email"]
            val password = body["password"]
            val displayName = body["displayName"]

            if (email == null || password == null || displayName == null) {
                return@post respondWithFailure("User registration failed", call)
            }

            val user = userRepository.findUserByEmail(email)
            if (user != null) {
                return@post respondWithFailure("User registration failed", call)
            }

            val newUser = userRepository.addUser(email, displayName, password)!!

            call.sessions.set(UserSession(newUser.id.value))
            call.respond(Response.success("User registered successfully", newUser.toDto()))
        } catch (e: Throwable) {
            application.log.error("Error in route ${call.request.uri}", e)
            respondWithFailure("User registration failed", call)
        }
    }

    post<UserLocations.Login> {
        try {
            val body = call.receive<Parameters>()
            val password = body["password"]
            val email = body["email"]

            if (password == null || email == null) return@post respondWithFailure("User login failed", call)

            val user = userRepository.findUserByEmail(email)

            if (userUtils.comparePasswords(password, user?.passwordHash)) {
                call.sessions.set(UserSession(user!!.id.value))
                call.respond(Response.success("User authenticated successfully", user.toDto()))
            } else {
                respondWithFailure("User login failed", call)
            }
        } catch (e: Throwable) {
            application.log.error("Error in route ${call.request.uri}", e)
            respondWithFailure("User login failed", call)
        }
    }

    get<UserLocations.Logout> {
        try {
            call.sessions.clear<UserSession>()
            call.respond(Response.success<String>("Logout success"))
        } catch (e: Throwable) {
            application.log.error("Error in route ${call.request.uri}", e)
            respondWithFailure("User logout failed", call)
        }
    }
}


