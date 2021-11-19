package org.codingforanimals.veganacademy

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.server.netty.*
import org.codingforanimals.veganacademy.config.configureApp
import org.codingforanimals.veganacademy.database.DatabaseFactory
import org.codingforanimals.veganacademy.di.appModule
import org.koin.core.module.Module
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit = EngineMain.main(args)

@KtorExperimentalLocationsAPI
fun Application.run(testing: Boolean = false, koinModules: List<Module> = listOf(appModule)) {
    configureApp(koinModules)

    val databaseFactory by inject<DatabaseFactory>(); databaseFactory.connect()

}