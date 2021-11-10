package org.codingforanimals.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*

fun Application.configureGson() {
    install(ContentNegotiation) {
        gson()
    }
}