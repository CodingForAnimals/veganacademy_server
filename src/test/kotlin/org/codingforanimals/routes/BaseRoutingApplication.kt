package org.codingforanimals.routes

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.locations.*
import io.ktor.server.testing.*
import io.ktor.util.reflect.*
import io.mockk.mockk
import org.codingforanimals.plugins.DatabaseFactory
import org.codingforanimals.plugins.configureSecurity
import org.jetbrains.exposed.sql.Database

fun Application.test() {

}

fun <R> withBaseTestApplication(test: TestApplicationEngine.() -> R) {
    withTestApplication ({
        configureSecurity()
        install(Locations)
        install(ContentNegotiation) { gson { } }
    }) {
        test()
    }
}

private fun initDB() {

}