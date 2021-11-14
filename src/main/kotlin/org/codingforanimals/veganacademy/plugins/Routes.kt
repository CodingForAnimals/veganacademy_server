package org.codingforanimals.veganacademy.plugins

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import org.codingforanimals.veganacademy.routes.userRoutes

@KtorExperimentalLocationsAPI
fun Application.configureRoutes() {

    routing {
        userRoutes()
        get("/") {
            call.respondText("This is a sample ktor backend!")
        }
    }
}
