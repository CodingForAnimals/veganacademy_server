package org.codingforanimals.auth

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

val algorithm = "HmacSHA1"
val hashKey = hex(System.getenv("SECRET_KEY"))
val hmacKey = SecretKeySpec(hashKey, algorithm)
fun hash(password: String): String {
    val hMac = Mac.getInstance(algorithm)
    hMac.init(hmacKey)
    return hex(hMac.doFinal(password.toByteArray(Charsets.UTF_8)))
}