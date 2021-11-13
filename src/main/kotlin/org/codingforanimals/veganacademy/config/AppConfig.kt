package org.codingforanimals.veganacademy.config

class AppConfig {
    lateinit var serverConfig: ServerConfig
    lateinit var jwtConfig: JwtConfig
    lateinit var databaseConfig: DatabaseConfig
}

data class ServerConfig(
    val isProd: Boolean
)

data class JwtConfig(
    val issuer: String,
    val secret: String
)

data class DatabaseConfig(
    val jdbcDriver: String,
    val jdbcDatabaseUrl: String,
    val dbUser: String,
    val dbPassword: String,
)