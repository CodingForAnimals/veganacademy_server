package org.codingforanimals.veganacademy.server.features.model.service.impl

import org.codingforanimals.veganacademy.server.features.model.repository.RememberMeRepository
import org.codingforanimals.veganacademy.server.features.model.repository.UserRepository
import org.codingforanimals.veganacademy.server.features.model.service.UserService
import org.codingforanimals.veganacademy.server.features.model.service.UserService.Companion.MESSAGE_LOGIN_FAILURE
import org.codingforanimals.veganacademy.server.features.model.service.UserService.Companion.MESSAGE_LOGIN_SUCCESS
import org.codingforanimals.veganacademy.server.features.model.service.UserService.Companion.MESSAGE_LOGIN_SUCCESS_REMEMBER_ME_FAILED
import org.codingforanimals.veganacademy.server.features.model.service.UserService.Companion.MESSAGE_REGISTRATION_FAILURE_USER_ALREADY_EXISTS
import org.codingforanimals.veganacademy.server.features.model.service.UserService.Companion.MESSAGE_REGISTRATION_SUCCESS
import org.codingforanimals.veganacademy.server.features.model.service.UserService.Companion.MESSAGE_REGISTRATION_SUCCESS_REMEMBER_ME_FAILED
import org.codingforanimals.veganacademy.server.features.routes.common.Response
import org.codingforanimals.veganacademy.server.features.routes.user.UserLoginRegisterResponse
import org.codingforanimals.veganacademy.server.features.routes.user.UserLoginRequest
import org.codingforanimals.veganacademy.server.features.routes.user.UserRegisterRequest

class UserServiceImpl(
    private val userRepository: UserRepository,
    private val rememberMeRepository: RememberMeRepository
) : UserService {

    override suspend fun login(request: UserLoginRequest): Response<UserLoginRegisterResponse> {
        val userDTO =
            userRepository.login(request.email, request.password) ?: return Response.failure(MESSAGE_LOGIN_FAILURE)

        val rememberMeCredentials = rememberMeRepository.getRememberMeCredentials(userDTO.userId, request.deviceUUID)
            ?: rememberMeRepository.addRememberMeCredentials(userDTO.userId, request.deviceUUID)

        if (rememberMeCredentials?.userToken == null) {
            val response = UserLoginRegisterResponse(null, userDTO)
            return Response.success(MESSAGE_LOGIN_SUCCESS_REMEMBER_ME_FAILED, response)
        }

        val response = UserLoginRegisterResponse(rememberMeCredentials.userToken, userDTO)
        return Response.success(MESSAGE_LOGIN_SUCCESS, response)
    }

    override suspend fun register(request: UserRegisterRequest): Response<UserLoginRegisterResponse> {
        val userDTO = userRepository.register(request.email, request.password, request.displayName)
            ?: return Response.failure(MESSAGE_REGISTRATION_FAILURE_USER_ALREADY_EXISTS)

        val rememberMeCredentials = rememberMeRepository.addRememberMeCredentials(userDTO.userId, request.deviceUUID)
            ?: return Response.success(MESSAGE_REGISTRATION_SUCCESS_REMEMBER_ME_FAILED)

        val response = UserLoginRegisterResponse(rememberMeCredentials.userToken, userDTO)
        return Response.success(MESSAGE_REGISTRATION_SUCCESS, response)
    }
}