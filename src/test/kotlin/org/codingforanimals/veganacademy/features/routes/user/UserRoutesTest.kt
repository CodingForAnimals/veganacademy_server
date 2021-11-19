package org.codingforanimals.veganacademy.features.routes.user

import com.google.gson.GsonBuilder
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.server.testing.*
import kotlinx.coroutines.launch
import org.codingforanimals.veganacademy.features.routes.common.Response
import org.junit.Test
import org.koin.test.AutoCloseKoinTest
import testutils.withTestServer
import kotlin.test.*

@KtorExperimentalLocationsAPI
internal class UserRoutesTest : AutoCloseKoinTest() {

    private val gson = GsonBuilder().create()

    private val registerBody = listOf(
        "email" to "email",
        "password" to "password",
        "displayName" to "displayName"
    ).formUrlEncode()

    @Test
    fun `given unique email, when registering, then user is added`() = withTestServer {
        val href = application.locations.href(
            UserRoutes.Register(parent = UserRoutes())
        )

        handleRequest(HttpMethod.Post, href) {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(registerBody)
        }.apply {
            val res = gson.fromJson(response.content!!, Response::class.java)
            assertTrue(res.success)
            assertNotNull(res.content)
        }
    }

    @Test
    fun `given user already exists, when registering, then respond failure`() = withTestServer {
        val href = application.locations.href(
            UserRoutes.Register(parent = UserRoutes())
        )

        handleRequest(HttpMethod.Post, href) {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(registerBody)
        }.apply {
            handleRequest(HttpMethod.Post, href) {
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(registerBody)
            }.apply {
                val res = gson.fromJson(response.content!!, Response::class.java)
                assertFalse(res.success)
                assertNull(res.content)
            }
        }


    }

}