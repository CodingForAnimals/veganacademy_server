package org.codingforanimals.veganacademy.server

import io.ktor.application.Application
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.netty.EngineMain
import org.codingforanimals.veganacademy.server.config.configureApp
import org.codingforanimals.veganacademy.server.database.DatabaseFactory
import org.codingforanimals.veganacademy.server.di.dataModules
import org.codingforanimals.veganacademy.server.di.sharedModules
import org.koin.core.module.Module
import org.koin.ktor.ext.inject

private val modules = sharedModules + dataModules

fun main(args: Array<String>): Unit = EngineMain.main(args)

@KtorExperimentalLocationsAPI
fun Application.run(koinModules: List<Module> = modules) {
    configureApp(koinModules)

    val databaseFactory by inject<DatabaseFactory>()
        databaseFactory.connect()
}