package org.codingforanimals.veganacademy.server.features.routes.common

data class Request<T> (
    var content: T
)