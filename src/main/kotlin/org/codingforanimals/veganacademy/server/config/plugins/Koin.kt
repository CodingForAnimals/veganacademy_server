package org.codingforanimals.veganacademy.server.config.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.core.module.Module
import org.koin.environmentProperties
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin(koinModules: List<Module>) {
    install(Koin) {
        modules(koinModules)
        slf4jLogger()
    }
}
