package org.codingforanimals.veganacademy.server.features.routes.user

import io.ktor.http.HttpMethod
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import org.codingforanimals.veganacademy.server.features.model.service.UserService
import org.codingforanimals.veganacademy.server.features.routes.common.Request
import org.junit.Test
import org.koin.test.AutoCloseKoinTest
import testutils.LoginLocation
import testutils.RegisterLocation
import testutils.getParsedResponse
import testutils.setContentTypeFormUrlEncoded
import testutils.toJson
import testutils.withTestServer
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@KtorExperimentalLocationsAPI
internal class LoginRouteTest : AutoCloseKoinTest() {

    private val user = UserLoginRequest("uuid", "email123", "password123")
    private val loginRequest = Request(user).toJson()

    @Test
    fun `given user doesn't exist, when logging in, then response message indicates so`() = withTestServer {
        handleRequest(HttpMethod.Post, LoginLocation()) {
            setContentTypeFormUrlEncoded()
            setBody(loginRequest)
        }.apply {
            val response = getParsedResponse<UserLoginRegisterResponse>()
            assertFalse(response.success)
            assertNull(response.content)
            assertEquals(UserService.MESSAGE_LOGIN_FAILURE, response.message)
        }
    }

    @Test
    fun `given user doesn't exist and correct credentials, when registering and logging in, then respond verify user data is sent accordingly`() =
        withTestServer {
            val userRequest = UserRegisterRequest("uuid", user.email, user.password, "name")
            val request = Request(userRequest).toJson()

            handleRequest(HttpMethod.Post, RegisterLocation()) {
                setContentTypeFormUrlEncoded()
                setBody(request)
            }.apply {
                handleRequest(HttpMethod.Post, LoginLocation()) {
                    setContentTypeFormUrlEncoded()
                    setBody(loginRequest)
                }.apply {
                    val res = getParsedResponse<UserLoginRegisterResponse>()
                    assertTrue(res.success)
                    assertNotNull(res.content)
                    assertNotNull(res.content?.rememberMeToken)
                    assertEquals(user.email, res.content?.user?.email)
                    assertEquals(UserService.MESSAGE_LOGIN_SUCCESS, res.message)
                }
            }
        }
}