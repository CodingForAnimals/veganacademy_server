package org.codingforanimals.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.codingforanimals.API_VERSION
import org.codingforanimals.main
import kotlin.test.Test
import kotlin.test.assertEquals


class UserRouteTest {

    @Test
    fun `asdasd dwdw`() {
        withTestApplication {
            handleRequest(HttpMethod.Get, USER_GET_ALL) {
                assertEquals("", "")
            }
        }
    }

    @Test
    fun `asd wdw`() {
        withBaseTestApplication {

            with(handleRequest(HttpMethod.Post, USER_LOGIN) {
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(listOf("email" to "agusmagne@gmail.com", "password" to "123123").formUrlEncode())
            }) {
                assertEquals("string", response.content)
            }
        }
    }
}