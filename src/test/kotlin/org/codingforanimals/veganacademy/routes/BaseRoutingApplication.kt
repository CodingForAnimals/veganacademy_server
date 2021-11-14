package org.codingforanimals.veganacademy.routes

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.locations.*
import io.ktor.server.testing.*
import org.codingforanimals.veganacademy.plugins.configureSecurity

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