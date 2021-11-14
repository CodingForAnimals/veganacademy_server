package org.codingforanimals.veganacademy.config.plugins

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import org.codingforanimals.veganacademy.config.auth.JwtService
import org.codingforanimals.veganacademy.features.model.repository.UserRepository
import org.koin.ktor.ext.inject

fun Application.configureAuth() {
    val jwtService by inject<JwtService>()
    val userRepository by inject<UserRepository>()

    install(Authentication) {
        jwt("jwt") {
            verifier(jwtService.verifier)
            realm = "VeganAcademy Server"
            validate { credential ->
                val id = credential.payload.getClaim("id").asInt()
                if (userRepository.findUserById(id) != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}