package org.codingforanimals.plugins

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.request.*
import org.codingforanimals.routes.userRoutes

@KtorExperimentalLocationsAPI
fun Application.configureRoutes() {

    routing {
        userRoutes()
        get("/") {
            call.respondText("This is a sample ktor backend!")
        }
    }
}
