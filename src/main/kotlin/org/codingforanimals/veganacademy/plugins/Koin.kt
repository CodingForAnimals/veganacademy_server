package org.codingforanimals.veganacademy.plugins

import io.ktor.application.*
import org.koin.core.module.Module
import org.koin.ktor.ext.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin(koinModules: List<Module>) {
    install(Koin) {
        slf4jLogger()
        modules(koinModules)
    }
}