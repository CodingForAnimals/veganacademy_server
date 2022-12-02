package org.codingforanimals.veganacademy.server.config.plugins

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import org.codingforanimals.veganacademy.server.features.routes.recipes.recipeRoutes
import org.codingforanimals.veganacademy.server.features.routes.user.rememberme.rememberMeRoutes
import org.codingforanimals.veganacademy.server.features.routes.user.userRoutes

fun Application.configureRoutes() {
    routing {
        userRoutes()
        rememberMeRoutes()
        recipeRoutes()
    }
}
