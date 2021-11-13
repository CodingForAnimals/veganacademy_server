package org.codingforanimals.veganacademy.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import org.codingforanimals.veganacademy.AppConfig
import org.codingforanimals.veganacademy.model.entity.User
import org.mindrot.jbcrypt.BCrypt
import java.util.*

class JwtService(appConfig: AppConfig) {

    private val issuer = appConfig.jwtConfig.issuer
    private val jwtSecret = appConfig.jwtConfig.secret
    private val algorithm = Algorithm.HMAC512(jwtSecret)

    val verifier: JWTVerifier = JWT.require(algorithm).withIssuer(issuer).build()

    fun hash(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun validate(password: String, passwordHash: String): Boolean {
        return BCrypt.checkpw(password, passwordHash)
    }

    fun generateToken(user: User): String {
        return JWT.create().withSubject("Authentication").withIssuer(issuer).withClaim("id", user.userId)
            .withExpiresAt(expirationDate()).sign(algorithm)
    }

    private fun expirationDate(): Date {
        return Date(System.currentTimeMillis() + 3_600_000 * 24)
    }

}