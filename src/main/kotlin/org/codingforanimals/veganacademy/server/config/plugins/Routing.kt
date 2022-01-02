package org.codingforanimals.veganacademy.server.config.plugins

import io.ktor.application.Application
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.routing.routing
import org.codingforanimals.veganacademy.server.features.routes.recipes.recipeRoutes
import org.codingforanimals.veganacademy.server.features.routes.user.userRoutes

@KtorExperimentalLocationsAPI
fun Application.configureRoutes() {
    routing {
        userRoutes()
        recipeRoutes()
    }
}
