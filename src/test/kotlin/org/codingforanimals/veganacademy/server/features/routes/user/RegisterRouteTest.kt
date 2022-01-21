package org.codingforanimals.veganacademy.server.features.routes.user

import com.google.gson.Gson
import io.ktor.http.HttpMethod
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import org.codingforanimals.veganacademy.server.features.model.service.UserService
import org.codingforanimals.veganacademy.server.features.routes.common.Request
import org.junit.Test
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject
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
class RegisterRouteTest {

    private val registerRequest = UserRegisterRequest("uuid", "email123", "password", "displayname123")
    private val request = Request(registerRequest).toJson()

    @Test
    fun `given user doesn't exist, when registering, then respond with correct data`() = withTestServer {
        handleRequest(HttpMethod.Post, RegisterLocation()) {
            setContentTypeFormUrlEncoded()
            setBody(request)
        }.apply {
            val res = getParsedResponse<UserLoginRegisterResponse>()
            assertTrue(res.success)
            assertNotNull(res.content)
            assertEquals(registerRequest.email, res.content?.user?.email)
            assertEquals(registerRequest.displayName, res.content?.user?.displayName)
            assertEquals(UserService.MESSAGE_REGISTRATION_SUCCESS, res.message)
        }
    }

    @Test
    fun `given user already exists, when registering, then respond with user already exists`() = withTestServer {
        handleRequest(HttpMethod.Post, RegisterLocation()) {
            setContentTypeFormUrlEncoded(this)
            setBody(request)
        }.apply {
            handleRequest(HttpMethod.Post, RegisterLocation()) {
                setContentTypeFormUrlEncoded(this)
                setBody(this@RegisterRouteTest.request)
            }.apply {
                val res = getParsedResponse<UserLoginRegisterResponse>()
                assertFalse(res.success)
                assertNull(res.content)
                assertEquals(UserService.MESSAGE_REGISTRATION_FAILURE_USER_ALREADY_EXISTS, res.message)
            }
        }
    }
}