package org.codingforanimals.veganacademy.config.plugins

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Principal
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie

fun Application.configureSessions(isProd: Boolean) {
    install(Sessions) {
        cookie<UserSession>(USER_SESSION) {
            cookie.secure = isProd
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60 * 5
        }
    }
}

data class UserSession(val userId: Int) : Principal

private const val USER_SESSION = "USER_SESSION"