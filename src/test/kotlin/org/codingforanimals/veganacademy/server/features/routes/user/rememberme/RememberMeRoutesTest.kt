package org.codingforanimals.veganacademy.server.features.routes.user.rememberme

import io.ktor.http.HttpMethod
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import org.codingforanimals.veganacademy.server.features.model.data.dto.RememberMeCredentialsDTO
import org.codingforanimals.veganacademy.server.features.model.service.RememberMeService
import org.codingforanimals.veganacademy.server.features.routes.common.Request
import org.codingforanimals.veganacademy.server.features.routes.user.UserLoginRegisterResponse
import org.codingforanimals.veganacademy.server.features.routes.user.UserLoginRequest
import org.codingforanimals.veganacademy.server.features.routes.user.UserRegisterRequest
import org.junit.Test
import testutils.RememberMeLocations.RememberMeLocation
import testutils.UserLocations.UserRegisterLocation
import testutils.getParsedResponse
import testutils.toJson
import testutils.withTestServer
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@KtorExperimentalLocationsAPI
class RememberMeRoutesTest {

    val userLoginData = UserLoginRequest("uuid", "email123", "password123")
    val loginRequest = Request(userLoginData).toJson()

    val userRegisterData = UserRegisterRequest("uuid", userLoginData.email, userLoginData.password, "name")
    val registerRequest = Request(userRegisterData).toJson()

    private val request = Request(
        RememberMeCredentialsDTO(
            userId = 1,
            userDeviceUUID = "user_device_UUID",
            userToken = "user_token"
        )
    ).toJson()

    @Test
    fun `given incorrect credentials, when asking for rememberMeCredentials, send failure response`() = withTestServer {
        handleRequest(HttpMethod.Post, RememberMeLocation()) {
            setBody(request)
        }.apply {
            val res = getParsedResponse<RememberMeCredentialsDTO>()

            assertNull(res.content)
            assertFalse(res.success)
            assertEquals(RememberMeService.MESSAGE_LOGGED_IN_FAILURE, res.message)
        }
    }

    @Test
    fun `given credentials exist, when asking for rememberMeCredentials, send success response with correct content`() =
        withTestServer {
            handleRequest(HttpMethod.Post, UserRegisterLocation()) {
                setBody(registerRequest)
            }.apply {
                handleRequest(HttpMethod.Post, RememberMeLocation()) {
                    val res = getParsedResponse<UserLoginRegisterResponse>()
                    setBody(createRememberMeRequest(res.content!!, userRegisterData.deviceUUID))
                }.apply {
                    val res = getParsedResponse<RememberMeCredentialsDTO>()

                    assertNotNull(res.content)
                    assertTrue(res.success)
                    assertEquals(RememberMeService.MESSAGE_LOGGED_IN_SUCCESS, res.message)
                    assertEquals(userRegisterData.deviceUUID, res.content?.userDeviceUUID)
                }
            }
        }

    private fun createRememberMeRequest(response: UserLoginRegisterResponse, deviceUUID: String) = Request(
        RememberMeCredentialsDTO(
            userId = response.user.userId,
            userDeviceUUID = deviceUUID,
            userToken = response.rememberMeToken!!
        )
    ).toJson()
}