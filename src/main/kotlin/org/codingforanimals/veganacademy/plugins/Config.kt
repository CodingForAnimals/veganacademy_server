package org.codingforanimals.veganacademy.plugins

import io.ktor.application.*
import org.codingforanimals.veganacademy.config.AppConfig
import org.codingforanimals.veganacademy.config.DatabaseConfig
import org.codingforanimals.veganacademy.config.JwtConfig
import org.codingforanimals.veganacademy.config.ServerConfig
import org.koin.ktor.ext.inject

fun Application.setupConfig() {
    val appConfig by inject<AppConfig>()

    val serverObject = environment.config.config("ktor.server")
    val isProd = serverObject.property("isProd").getString().toBoolean()
    appConfig.serverConfig = ServerConfig(isProd)

    val jwtObject = environment.config.config("ktor.jwt")
    val issuer = jwtObject.property("issuer").getString()
    val secret = jwtObject.property("secret").getString()
    appConfig.jwtConfig = JwtConfig(issuer, secret)

    val databaseObject = environment.config.config("ktor.database")
    val jdbcDriver = databaseObject.property("jdbcDriver").getString()
    val jdbcDatabaseUrl = databaseObject.property("jdbcDatabaseUrl").getString()
    val dbUser = databaseObject.property("dbUser").getString()
    val dbPassword = databaseObject.property("dbPassword").getString()
    appConfig.databaseConfig = DatabaseConfig(jdbcDriver, jdbcDatabaseUrl, dbUser, dbPassword)
}