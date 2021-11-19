package org.codingforanimals.veganacademy.features.routes.user

import com.google.gson.GsonBuilder
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.server.testing.*
import org.codingforanimals.veganacademy.features.routes.common.Response
import org.junit.Test
import testutils.setContentType
import testutils.withTestServer
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@KtorExperimentalLocationsAPI
class RegisterRouteTest {

    fun registerHref(engine: TestApplicationEngine) = engine.application.locations.href(
        UserRoutes.Register(parent = UserRoutes())
    )

    private val gson = GsonBuilder().create()

    private val userBody = listOf(
        "email" to "email",
        "password" to "password",
        "displayName" to "displayName"
    ).formUrlEncode()

    @Test
    fun `given unique email, when registering, then user is added`() = withTestServer {
        handleRequest(HttpMethod.Post, registerHref(this)) {
            setContentType(this)
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
            setContentType(this)
            setBody(userBody)
        }.apply {
            handleRequest(HttpMethod.Post, registerHref(this@withTestServer)) {
                setContentType(this)
                setBody(userBody)
            }.apply {
                val res = gson.fromJson(response.content!!, Response::class.java)
                assertFalse(res.success)
            }
        }
    }
}