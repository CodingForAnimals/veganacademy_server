package org.codingforanimals.veganacademy.server.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

class DatabaseFactoryForUnitTest: DatabaseFactory {

    lateinit var source: HikariDataSource

    override fun connect() {
        Database.connect(hikari())
        SchemaDefinition.createSchema()
    }

    override fun close() {
        source.close()
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.h2.Driver"
        config.jdbcUrl = "jdbc:h2:mem:;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE"
        config.maximumPoolSize = 2
        config.isAutoCommit = true
        config.validate()
        source = HikariDataSource(config)
        return source
    }
}