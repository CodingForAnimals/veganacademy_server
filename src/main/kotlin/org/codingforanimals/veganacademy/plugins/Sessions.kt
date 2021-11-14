package org.codingforanimals.veganacademy.plugins

import io.ktor.application.*
import io.ktor.sessions.*
import org.codingforanimals.veganacademy.auth.MySession

fun Application.configureSessions() {
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
}