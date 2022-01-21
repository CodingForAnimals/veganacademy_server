package testutils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.server.testing.TestApplicationCall
import io.ktor.server.testing.TestApplicationRequest
import org.codingforanimals.veganacademy.server.features.routes.common.Request
import org.codingforanimals.veganacademy.server.features.routes.common.Response

val gson = Gson()

inline fun <reified T> TestApplicationCall.getParsedResponse(): Response<T> {
    val responseType = object : TypeToken<Response<T>>() {}.type
    return gson.fromJson(response.content, responseType)
}

fun TestApplicationRequest.setContentTypeFormUrlEncoded() =
    addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())

inline fun <reified T> Request<T>.toJson(): String = gson.toJson(this)