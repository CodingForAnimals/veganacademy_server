package org.codingforanimals.veganacademy.server.config.plugins

import io.ktor.server.application.Application
import org.koin.ktor.ext.inject

fun Application.setupConfig() {
    val appConfig by inject<AppConfig>()

    val serverObject = environment.config.config("ktor.server")
    appConfig.serverConfig = ServerConfig(
        isProd = serverObject.property("isProd").getString().toBoolean(),
        isTesting = serverObject.property("isTesting").getString().toBoolean(),
    )

    appConfig.databaseConfig = if (appConfig.serverConfig.isTesting) {
        DatabaseConfig(
            jdbcDriver = "org.h2.Driver",
            maxPoolSize = 1,
            jdbcDatabaseUrl = "jdbc:h2:mem:test",
            dbUser = "root",
            dbPassword = "password",
        )
    } else {
        val databaseObject = environment.config.config("ktor.database")
        DatabaseConfig(
            jdbcDriver = databaseObject.property("jdbcDriver").getString(),
            maxPoolSize = databaseObject.property("maxPoolSize").getString().toInt(),
            jdbcDatabaseUrl = System.getenv("DB_URL").toString(),
            dbUser = System.getenv("DB_USER").toString(),
            dbPassword = System.getenv("DB_PASS").toString(),
        )
    }
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