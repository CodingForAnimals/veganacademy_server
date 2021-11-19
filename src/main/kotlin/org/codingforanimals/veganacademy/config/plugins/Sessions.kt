package org.codingforanimals.veganacademy.config.plugins

import io.ktor.application.*
import io.ktor.sessions.*
import org.codingforanimals.veganacademy.features.routes.user.MySession

fun Application.configureSessions() {
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
}