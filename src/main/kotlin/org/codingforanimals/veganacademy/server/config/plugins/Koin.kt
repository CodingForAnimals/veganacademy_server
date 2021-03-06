package org.codingforanimals.veganacademy.server.config.plugins

import io.ktor.application.*
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.modules
import org.koin.logger.slf4jLogger

fun Application.configureKoin(koinModules: List<Module>) {
    install(Koin) {
        modules(koinModules)
        slf4jLogger()
    }
}
