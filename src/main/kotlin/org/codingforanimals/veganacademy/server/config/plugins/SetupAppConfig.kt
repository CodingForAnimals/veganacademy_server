package org.codingforanimals.veganacademy.server.config.plugins

import io.ktor.application.*
import org.koin.ktor.ext.inject

fun Application.setupConfig() {
    val appConfig by inject<AppConfig>()

    val serverObject = environment.config.config("ktor.server")
    val isProd = serverObject.property("isProd").getString().toBoolean()
    val isTesting = serverObject.property("isTesting").getString().toBoolean()
    appConfig.serverConfig = ServerConfig(isProd, isTesting)

    val databaseObject = environment.config.config("ktor.database")
    val jdbcDriver = databaseObject.property("jdbcDriver").getString()
    val jdbcDatabaseUrl = databaseObject.property("jdbcDatabaseUrl").getString()
    val dbUser = databaseObject.property("dbUser").getString()
    val dbPassword = databaseObject.property("dbPassword").getString()
    val maxPoolSize = databaseObject.property("maxPoolSize").getString().toInt()
    appConfig.databaseConfig = DatabaseConfig(jdbcDriver, jdbcDatabaseUrl, dbUser, dbPassword, maxPoolSize)
}

class AppConfig {
    lateinit var serverConfig: ServerConfig
    lateinit var databaseConfig: DatabaseConfig
}

data class ServerConfig(
    val isProd: Boolean,
    val isTesting: Boolean,
)

data class DatabaseConfig(
    val jdbcDriver: String,
    val jdbcDatabaseUrl: String,
    val dbUser: String,
    val dbPassword: String,
    val maxPoolSize: Int,
)