package org.codingforanimals.veganacademy

import io.ktor.application.Application
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.netty.EngineMain
import org.codingforanimals.veganacademy.config.configureApp
import org.codingforanimals.veganacademy.database.DatabaseFactory
import org.codingforanimals.veganacademy.di.appModule
import org.koin.core.module.Module
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit = EngineMain.main(args)

@KtorExperimentalLocationsAPI
fun Application.run(isProd: Boolean = false, koinModules: List<Module> = listOf(appModule)) {
    configureApp(isProd, koinModules)

    val databaseFactory by inject<DatabaseFactory>()
    databaseFactory.connect()
}