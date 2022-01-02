package org.codingforanimals.veganacademy.server.config.plugins

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.session
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import org.codingforanimals.veganacademy.server.features.model.repository.UserRepository
import org.codingforanimals.veganacademy.server.features.routes.common.Response
import org.koin.ktor.ext.inject

fun Application.configureAuth() {
    val userRepository by inject<UserRepository>()

    install(Authentication) {
        session<UserSession>(AUTH_SESSION) {
            validate { session ->
                val user = userRepository.findUserById(session.userId)
                if (user != null) {
                    session
                } else {
                    null
                }
            }

            challenge {
                call.respond(HttpStatusCode.BadRequest, Response.failure<String>("Authentication failed"))
            }
        }
    }
}

const val AUTH_SESSION = "AUTH_SESSION"
const val AUTH_FORM = "AUTH-FORM"