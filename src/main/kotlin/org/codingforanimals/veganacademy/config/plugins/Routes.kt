package org.codingforanimals.veganacademy.config.plugins

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import org.codingforanimals.veganacademy.features.routes.user.userRoutes

@KtorExperimentalLocationsAPI
fun Application.configureRoutes() {

    routing {
        userRoutes()
        get("/") {
            call.respondText("This is a sample ktor backend!")
        }
    }
}
