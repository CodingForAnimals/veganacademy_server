package org.codingforanimals

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import org.codingforanimals.plugins.*
import org.codingforanimals.routes.userRoutes
import org.koin.core.annotation.KoinReflectAPI
import org.koin.core.module.Module
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.koin.logger.slf4jLogger
import org.slf4j.LoggerFactory

fun main(args: Array<String>): Unit = EngineMain.main(args)

@KtorExperimentalLocationsAPI
@KoinReflectAPI
fun Application.module(testing: Boolean = false, koinModules: List<Module> = listOf(appModule)) {
    configureKoin(koinModules)
    setupConfig()
    configureAuth()
    configureGson()
    configureLogging()
    configureLocations()
    configureRoutes()
}

//
//@KtorExperimentalLocationsAPI
//fun main() {
//    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
//        setupConfig()
//        configureSecurity()
//        install(Locations)
//        install(ContentNegotiation) { gson {  }}
//
//        DatabaseFactory.init()
//        val usersRepository = UsersRepository()
//
//        val jwtService = JwtService()
//        val hashFunction = { s: String -> hash(s) }
//
//        install(Authentication) {
//            jwt("jwt") {
//                verifier(jwtService.verifier)
//                realm = "VeganAcademy Server"
//                validate {
//                    val payload = it.payload
//                    val claim = payload.getClaim("id")
//                    val claimInt = claim.asInt()
//                    val user = usersRepository.findUserById(claimInt)
//                    user
//                }
//            }
//        }
//
//        routing {
//            users(usersRepository, jwtService, hashFunction)
//        }
//    }.start(wait = true)
//}
//
const val API_VERSION = "/v1"