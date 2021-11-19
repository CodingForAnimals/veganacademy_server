package org.codingforanimals.veganacademy.config

import io.ktor.application.*
import io.ktor.locations.*
import org.codingforanimals.veganacademy.config.plugins.*
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