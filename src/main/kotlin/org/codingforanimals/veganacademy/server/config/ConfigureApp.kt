package org.codingforanimals.veganacademy.server.config

import io.ktor.application.Application
import io.ktor.locations.KtorExperimentalLocationsAPI
import org.codingforanimals.veganacademy.server.config.plugins.configureAuth
import org.codingforanimals.veganacademy.server.config.plugins.configureGson
import org.codingforanimals.veganacademy.server.config.plugins.configureKoin
import org.codingforanimals.veganacademy.server.config.plugins.configureLocations
import org.codingforanimals.veganacademy.server.config.plugins.configureLogging
import org.codingforanimals.veganacademy.server.config.plugins.configureRoutes
import org.codingforanimals.veganacademy.server.config.plugins.configureSessions
import org.codingforanimals.veganacademy.server.config.plugins.setupConfig
import org.koin.core.module.Module

@KtorExperimentalLocationsAPI
fun Application.configureApp(koinModules: List<Module>) {
    configureKoin(koinModules)
    setupConfig()
    configureAuth()
    configureGson()
    configureLogging()
    configureLocations()
    configureRoutes()
    configureSessions()
}