package org.codingforanimals.veganacademy.server.config.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.locations.KtorExperimentalLocationsAPI
import io.ktor.server.locations.Locations

@KtorExperimentalLocationsAPI
fun Application.configureLocations() {
    install(Locations)
}