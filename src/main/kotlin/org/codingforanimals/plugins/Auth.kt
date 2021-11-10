package org.codingforanimals.plugins

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import org.codingforanimals.auth.JwtService
import org.codingforanimals.model.repository.UserRepository
import org.koin.ktor.ext.inject

fun Application.configureAuth() {
    val jwtService by inject<JwtService>()
    val userRepository by inject<UserRepository>()

    install(Authentication) {
        jwt("jwt") {
            verifier(jwtService.verifier)
            realm = "VeganAcademy Server"
            validate {
                val payload = it.payload
                val claim = payload.getClaim("id")
                val claimInt = claim.asInt()
                val user = userRepository.findUserById(claimInt)
                user
            }
        }
    }
}