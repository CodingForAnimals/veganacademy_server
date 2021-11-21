package org.codingforanimals.veganacademy.features.routes.user

import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.server.testing.*
import org.codingforanimals.veganacademy.features.routes.common.Response
import org.junit.Test
import org.koin.test.AutoCloseKoinTest
import testutils.gson
import testutils.setContentTypeFormUrlEncoded
import testutils.withTestServer
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@KtorExperimentalLocationsAPI
internal class AuthenticateRouteTest : AutoCloseKoinTest() {

    private val userBody = listOf(
        "email" to "email",
        "password" to "password",
        "displayName" to "displayName"
    ).formUrlEncode()

    private fun href(engine: TestApplicationEngine) = engine.application.locations.href(
        UserRoutes.Authenticate(parent = UserRoutes())
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
        val registerHref = application.locations.href(UserRoutes.Register(UserRoutes()))
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