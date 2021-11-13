package org.codingforanimals.veganacademy

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.server.netty.*
import org.codingforanimals.veganacademy.db.DatabaseFactory
import org.codingforanimals.veganacademy.plugins.*
import org.koin.core.annotation.KoinReflectAPI
import org.koin.core.module.Module
import org.koin.ktor.ext.get

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
    configureSessions()
    DatabaseFactory.init(get())
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