package org.codingforanimals.veganacademy.server.config.plugins

import io.ktor.application.*
import io.ktor.locations.*

fun Application.configureLocations() {
    install(Locations)
}