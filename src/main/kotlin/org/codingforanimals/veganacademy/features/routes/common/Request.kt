package org.codingforanimals.veganacademy.features.routes.common

data class Request<T> (
    var content: T
)