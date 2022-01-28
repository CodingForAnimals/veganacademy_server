package org.codingforanimals.veganacademy.server.config.plugins

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Principal
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import org.koin.ktor.ext.inject

fun Application.configureSessions() {
    val appConfig by inject<AppConfig>()
    install(Sessions) {
        cookie<UserSession>(USER_SESSION) {
            cookie.secure = appConfig.serverConfig.isProd
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60 * 5
        }
    }
}

data class UserSession(val userId: Int) : Principal

private const val USER_SESSION = "USER_SESSION"