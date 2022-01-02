package org.codingforanimals.veganacademy.server.features.routes.user

import io.ktor.http.HttpMethod
import io.ktor.http.formUrlEncode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.locations
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import org.codingforanimals.veganacademy.server.features.routes.common.Response
import org.junit.Test
import testutils.gson
import testutils.setContentTypeFormUrlEncoded
import testutils.withTestServer
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@KtorExperimentalLocationsAPI
class RegisterRouteTest {

    fun registerHref(engine: TestApplicationEngine) = engine.application.locations.href(
        UserLocations.Register(parent = UserLocations())
    )

    private val userBody = listOf(
        "email" to "email",
        "password" to "password",
        "displayName" to "displayName"
    ).formUrlEncode()

    @Test
    fun `given unique email, when registering, then user is added`() = withTestServer {
        handleRequest(HttpMethod.Post, registerHref(this)) {
            setContentTypeFormUrlEncoded(this)
            setBody(userBody)
        }.apply {
            val res = gson.fromJson(response.content!!, Response::class.java)
            assertTrue(res.success)
            assertNotNull(res.content)
        }
    }

    @Test
    fun `given user already exists, when registering, then respond failure`() = withTestServer {
        handleRequest(HttpMethod.Post, registerHref(this)) {
            setContentTypeFormUrlEncoded(this)
            setBody(userBody)
        }.apply {
            handleRequest(HttpMethod.Post, registerHref(this@withTestServer)) {
                setContentTypeFormUrlEncoded(this)
                setBody(userBody)
            }.apply {
                val res = gson.fromJson(response.content!!, Response::class.java)
                assertFalse(res.success)
            }
        }
    }
}