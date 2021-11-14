package org.codingforanimals.veganacademy

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.server.netty.*
import org.codingforanimals.veganacademy.config.configApp
import org.codingforanimals.veganacademy.database.DatabaseFactory
import org.codingforanimals.veganacademy.di.appModule
import org.codingforanimals.veganacademy.config.plugins.*
import org.koin.core.annotation.KoinReflectAPI
import org.koin.core.module.Module
import org.koin.ktor.ext.get

fun main(args: Array<String>): Unit = EngineMain.main(args)

@KtorExperimentalLocationsAPI
@KoinReflectAPI
fun Application.module(testing: Boolean = false, koinModules: List<Module> = listOf(appModule)) {
    configApp(koinModules)
    DatabaseFactory.init(get())
}