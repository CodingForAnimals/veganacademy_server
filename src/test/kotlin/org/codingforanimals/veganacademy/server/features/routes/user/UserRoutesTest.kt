package org.codingforanimals.veganacademy.server.features.routes.user

import io.ktor.http.HttpMethod
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import org.codingforanimals.veganacademy.server.config.plugins.UserSession
import org.codingforanimals.veganacademy.server.features.model.service.UserService
import org.codingforanimals.veganacademy.server.features.routes.common.Request
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import testutils.UserLocations.UserLoginLocation
import testutils.UserLocations.UserLogoutLocation
import testutils.UserLocations.UserRegisterLocation
import testutils.UserObjects.loginRequest
import testutils.UserObjects.registerRequest
import testutils.UserObjects.userLoginData
import testutils.UserObjects.userRegisterData
import testutils.getParsedResponse
import testutils.toJson
import testutils.withTestServer
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@KtorExperimentalLocationsAPI
@RunWith(Enclosed::class)
internal class UserRoutesTest : AutoCloseKoinTest() {

    class UserLoginRoutes {

        @Test
        fun `given user doesn't exist, when logging in, then response message send error`() = withTestServer {
            handleRequest(HttpMethod.Post, UserLoginLocation()) {
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
                handleRequest(HttpMethod.Post, UserRegisterLocation()) {
                    setBody(registerRequest)
                }.apply {
                    handleRequest(HttpMethod.Post, UserLoginLocation()) {
                        setBody(loginRequest)
                    }.apply {
                        val res = getParsedResponse<UserLoginRegisterResponse>()

                        assertTrue(res.success)
                        assertNotNull(res.content)
                        assertNotNull(res.content?.rememberMeToken)
                        assertEquals(userLoginData.email, res.content?.user?.email)
                        assertEquals(UserService.MESSAGE_LOGIN_SUCCESS, res.message)
                    }
                }
            }
    }

    class UserRegisterRoutes {

        @Test
        fun `given user doesn't exist, when registering, then respond with correct data`() = withTestServer {
            handleRequest(HttpMethod.Post, UserRegisterLocation()) {
                setBody(registerRequest)
            }.apply {
                val res = getParsedResponse<UserLoginRegisterResponse>()

                assertTrue(res.success)
                assertNotNull(res.content)
                assertEquals(userRegisterData.email, res.content?.user?.email)
                assertEquals(userRegisterData.displayName, res.content?.user?.displayName)
                assertEquals(UserService.MESSAGE_REGISTRATION_SUCCESS, res.message)
            }
        }

        @Test
        fun `given user already exists, when registering, then respond with user already exists`() = withTestServer {
            handleRequest(HttpMethod.Post, UserRegisterLocation()) {
                setBody(registerRequest)
            }.apply {
                handleRequest(HttpMethod.Post, UserRegisterLocation()) {
                    setBody(registerRequest)
                }.apply {
                    val res = getParsedResponse<UserLoginRegisterResponse>()

                    assertFalse(res.success)
                    assertNull(res.content)
                    assertEquals(UserService.MESSAGE_REGISTRATION_FAILURE_USER_ALREADY_EXISTS, res.message)
                }
            }
        }
    }

    class UserSessionRoutes {

        @Test
        fun `given a session is set, then response contains non null cookie`() = withTestServer {
            handleRequest(HttpMethod.Post, UserRegisterLocation()) {
                setBody(Request(userRegisterData).toJson())
            }.apply {
                val res = getParsedResponse<UserLoginRegisterResponse>()
                val session = sessions.get<UserSession>()

                assertNotNull(session)
                assertEquals(res.content?.user?.userId, session.userId)
            }
        }

        @Test
        fun `when clearing session, then return success with empty cookie`() = withTestServer {
            handleRequest(HttpMethod.Post, UserRegisterLocation()) {
                setBody(Request(userRegisterData).toJson())
            }.apply {
                handleRequest(HttpMethod.Get, UserLogoutLocation()).apply {
                    val res = getParsedResponse<Any>()
                    val session = sessions.get<UserSession>()

                    assertNull(res.content)
                    assertEquals(UserService.MESSAGE_LOGOUT_SUCCESS, res.message)
                    assertNull(session)
                }
            }
        }
    }
}