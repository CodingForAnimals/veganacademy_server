package org.codingforanimals.veganacademy.features.controller

import io.ktor.config.*
import io.ktor.locations.*
import io.ktor.server.testing.*
import org.codingforanimals.veganacademy.di.appModule
import org.codingforanimals.veganacademy.module
import org.koin.core.annotation.KoinReflectAPI
import kotlin.test.Test


@KoinReflectAPI
@KtorExperimentalLocationsAPI
class UserRouteTest {

    fun MapApplicationConfig.createConfigForTesting() {
        put("ktor.server.isProd", "false")
        put("ktor.jwt.issuer", "issuer")
        put("ktor.jwt.secret", "secret")
        put("ktor.database.jdbcDriver", "jdbcDriver")
        put("ktor.database.jdbcDatabaseUrl", "jdbcDatabaseUrl")
        put("ktor.database.dbUser", "dbUser")
        put("ktor.database.dbPassword", "dbPassword")
    }

    @Test
    fun test() = withTestApplication({
        (environment.config as MapApplicationConfig).apply {
            createConfigForTesting()
        }
        module(testing = true, koinModules = listOf(appModule))
    }) {
        println("asd")
    }
}