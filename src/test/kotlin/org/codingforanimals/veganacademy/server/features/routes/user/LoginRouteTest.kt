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
import org.koin.test.AutoCloseKoinTest
import testutils.gson
import testutils.setContentTypeFormUrlEncoded
import testutils.withTestServer
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@KtorExperimentalLocationsAPI
internal class LoginRouteTest : AutoCloseKoinTest() {

    private val userBody = listOf(
        "email" to "email",
        "password" to "password",
        "displayName" to "displayName"
    ).formUrlEncode()

    private fun href(engine: TestApplicationEngine) = engine.application.locations.href(
        UserLocations.Login(parent = UserLocations())
    )

    @Test
    fun `given user doesn't exists, when authenticating, then respond failure`() = withTestServer {
        handleRequest(HttpMethod.Post, href(this)) {
            setContentTypeFormUrlEncoded(this)
            setBody(userBody)
        }.apply {
            val res = gson.fromJson(response.content!!, Response::class.java)
            assertFalse(res.success)
        }
    }

    @Test
    fun `given user exists, when authenticating, then respond success`() = withTestServer {
        val registerHref = application.locations.href(UserLocations.Register(UserLocations()))
        handleRequest(HttpMethod.Post, registerHref) {
            setContentTypeFormUrlEncoded(this)
            setBody(userBody)
        }.apply {
            handleRequest(HttpMethod.Post, href(this@withTestServer)) {
                setContentTypeFormUrlEncoded(this)
                setBody(userBody)
            }.apply {
                val res = gson.fromJson(response.content!!, Response::class.java)
                assertTrue(res.success)
                assertNotNull(res.content)
            }
        }
    }
}