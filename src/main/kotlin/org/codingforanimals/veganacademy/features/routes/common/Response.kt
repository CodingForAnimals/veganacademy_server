package org.codingforanimals.veganacademy.features.routes.common

class Response<T> private constructor(
    var success: Boolean,
    var message: String?,
    var content: T?,
){

    companion object {
        fun <T> success(message: String, content: T): Response<T> {
            return Response(
                success = true,
                message = message,
                content = content,
            )
        }

        fun <T> failure(message: String, content: T? = null): Response<T> {
            return Response(
                success = false,
                message = message,
                content = content
            )
        }
    }
}