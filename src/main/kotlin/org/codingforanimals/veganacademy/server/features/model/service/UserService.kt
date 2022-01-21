package org.codingforanimals.veganacademy.server.features.model.service

import org.codingforanimals.veganacademy.server.features.routes.common.Response
import org.codingforanimals.veganacademy.server.features.routes.user.UserLoginRegisterResponse
import org.codingforanimals.veganacademy.server.features.routes.user.UserLoginRequest
import org.codingforanimals.veganacademy.server.features.routes.user.UserRegisterRequest

interface UserService {

    suspend fun login(request: UserLoginRequest): Response<UserLoginRegisterResponse>
    suspend fun register(request: UserRegisterRequest): Response<UserLoginRegisterResponse>

    companion object {
        const val MESSAGE_REGISTRATION_FAILURE_USER_ALREADY_EXISTS = "User registration failed. User already exists."
        const val MESSAGE_REGISTRATION_SUCCESS = "User registration success"
        const val MESSAGE_REGISTRATION_SUCCESS_REMEMBER_ME_FAILED =
            "User registered successfully. Creation of remember me credentials failed."

        const val MESSAGE_LOGIN_FAILURE = "User login failed. User with such credentials doesn't exist"
        const val MESSAGE_LOGIN_SUCCESS = "User login success"
        const val MESSAGE_LOGIN_SUCCESS_REMEMBER_ME_FAILED = "User logged in successfully. "
    }
}