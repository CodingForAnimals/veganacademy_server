package org.codingforanimals.veganacademy.server.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.codingforanimals.veganacademy.server.config.plugins.AppConfig
import org.jetbrains.exposed.sql.Database

class DatabaseFactoryForServerTest(appConfig: AppConfig) : DatabaseFactory {

    private val dbConfig = appConfig.databaseConfig

    private lateinit var source: HikariDataSource

    override fun connect() {
        Database.connect(hikari())
        SchemaDefinition.createSchema()
    }

    override fun close() {
        source.close()
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = dbConfig.jdbcDriver
        config.jdbcUrl = dbConfig.jdbcDatabaseUrl
        config.maximumPoolSize = dbConfig.maxPoolSize
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        source = HikariDataSource(config)
        return source
    }
}