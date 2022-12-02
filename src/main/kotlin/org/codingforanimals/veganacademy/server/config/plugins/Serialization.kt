package org.codingforanimals.veganacademy.server.config.plugins

import io.ktor.serialization.gson.gson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun Application.configureGson() {
    install(ContentNegotiation) {
        gson()
    }
}