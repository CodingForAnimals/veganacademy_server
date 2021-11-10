package org.codingforanimals.plugins

import io.ktor.application.*
import io.ktor.locations.*

fun Application.configureLocations() {
    install(Locations)
}