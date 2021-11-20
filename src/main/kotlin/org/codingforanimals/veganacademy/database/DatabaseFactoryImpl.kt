package org.codingforanimals.veganacademy.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.codingforanimals.veganacademy.config.plugins.AppConfig
import org.jetbrains.exposed.sql.Database

class DatabaseFactoryImpl(appConfig: AppConfig) : DatabaseFactory {

    private val dbConfig = appConfig.databaseConfig

    override fun connect() {
        Database.connect(hikari())
        SchemaDefinition.createSchema()
    }

    override fun close() {
        TODO("Not yet implemented")
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = dbConfig.jdbcDriver
        config.jdbcUrl = dbConfig.jdbcDatabaseUrl
        config.username = dbConfig.dbUser
        config.password = dbConfig.dbPassword
        config.maximumPoolSize = dbConfig.maxPoolSize
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

        config.validate()
        return HikariDataSource(config)
    }

}