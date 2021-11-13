package org.codingforanimals.veganacademy.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.codingforanimals.veganacademy.AppConfig
import org.codingforanimals.veganacademy.model.entity.tables.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init(appConfig: AppConfig) {
        val jdbcDriver = appConfig.databaseConfig.jdbcDriver
        val jdbcDatabaseUrl = appConfig.databaseConfig.jdbcDatabaseUrl
        val dbUser = appConfig.databaseConfig.dbUser
        val dbPassword = appConfig.databaseConfig.dbPassword
        Database.connect(hikari(jdbcDriver, jdbcDatabaseUrl, dbUser, dbPassword))
        println("Connected to DB")

        transaction {
            SchemaUtils.create(Users)
        }
    }

    private fun hikari(
        jdbcDriver: String,
        jdbcDatabaseUrl: String,
        dbUser: String,
        dbPassword: String
    ): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = jdbcDriver
        config.jdbcUrl = jdbcDatabaseUrl
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.username = dbUser
        config.password = dbPassword
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }

}