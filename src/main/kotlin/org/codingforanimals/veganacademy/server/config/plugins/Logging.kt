package org.codingforanimals.veganacademy.server.config.plugins

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import io.ktor.application.*
import io.ktor.features.*
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory

fun Application.configureLogging() {

    install(CallLogging) {
        level = org.slf4j.event.Level.INFO
    }

    val appConfig by inject<AppConfig>()

    if (!appConfig.serverConfig.isProd) {
        val root = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME) as Logger
        root.level = Level.TRACE
    }
}