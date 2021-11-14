package org.codingforanimals.veganacademy.config.plugins

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.locations.*
import org.codingforanimals.veganacademy.features.controller.userController

@KtorExperimentalLocationsAPI
fun Application.configureRoutes() {
    routing {
        userController()
    }
}
