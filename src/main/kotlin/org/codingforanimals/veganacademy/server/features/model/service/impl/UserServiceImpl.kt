package org.codingforanimals.veganacademy.server.features.model.service.impl

import org.codingforanimals.veganacademy.server.features.model.dto.UserDTO
import org.codingforanimals.veganacademy.server.features.model.repository.UserRepository
import org.codingforanimals.veganacademy.server.features.model.service.UserService
import org.codingforanimals.veganacademy.server.features.routes.common.Response
import org.codingforanimals.veganacademy.server.features.routes.user.UserLoginRequest
import org.codingforanimals.veganacademy.server.features.routes.user.UserRegisterRequest

class UserServiceImpl(
    private val userRepository: UserRepository,
): UserService {

    companion object {
        private const val MESSAGE_REGISTRATION_FAILURE = "User registration failure"
        private const val MESSAGE_REGISTRATION_SUCCESS = "User registration success"
        private const val MESSAGE_LOGIN_FAILURE = "User login failure"
        private const val MESSAGE_LOGIN_SUCCESS = "User login success"
        private const val MESSAGE_LOGOUT_SUCCESS = "User logout success"
        private const val MESSAGE_LOGOUT_FAILURE = "User logout failure"
    }

    override suspend fun login(request: UserLoginRequest): Response<UserDTO> {
        val user = userRepository.login(request.email, request.password)
        return if (user == null) {
            Response.failure(MESSAGE_LOGIN_FAILURE)
        } else {
            Response.success(MESSAGE_LOGIN_SUCCESS, user)
        }
    }

    override suspend fun register(request: UserRegisterRequest): Response<UserDTO> {
        val userDTO = userRepository.register(request.email, request.password, request.displayName)
        return if (userDTO == null) {
            Response.failure(MESSAGE_REGISTRATION_FAILURE)
        } else {
            Response.success(MESSAGE_REGISTRATION_SUCCESS, userDTO)
        }
    }
}