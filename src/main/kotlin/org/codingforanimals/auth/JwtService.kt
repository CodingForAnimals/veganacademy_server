package org.codingforanimals.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import org.codingforanimals.model.entity.User
import java.util.*

class JwtService {

    private val issuer = "issuer"
    private val jwtSecret = System.getenv("JWT_SECRET")
    private val algorithm = Algorithm.HMAC512(jwtSecret)

    val verifier: JWTVerifier = JWT.require(algorithm).withIssuer(issuer).build()

    fun generateToken(user: User): String {
        return JWT.create().withSubject("Authentication").withIssuer(issuer).withClaim("id", user.userId).withExpiresAt(expiresAt()).sign(algorithm)
    }

    private fun expiresAt(): Date {
        return Date(System.currentTimeMillis() + 3_600_000 * 24)
    }

}