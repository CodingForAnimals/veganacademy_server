package org.codingforanimals.veganacademy.server.config.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.session
import io.ktor.server.response.respond
import org.codingforanimals.veganacademy.server.features.model.repository.UserRepository
import org.codingforanimals.veganacademy.server.features.routes.common.Response
import org.koin.ktor.ext.inject

fun Application.configureAuth() {
    val appConfig by inject<AppConfig>()
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

            if (!appConfig.serverConfig.isTesting) {
                challenge {
                    call.respond(HttpStatusCode.BadRequest, Response.failure<String>("Authentication failed"))
                }
            }
        }
    }
}

const val AUTH_SESSION = "AUTH_SESSION"