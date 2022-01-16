package org.codingforanimals.veganacademy.server.features.model.service

import org.codingforanimals.veganacademy.server.features.model.dto.UserDTO
import org.codingforanimals.veganacademy.server.features.routes.common.Response
import org.codingforanimals.veganacademy.server.features.routes.user.UserLoginRequest
import org.codingforanimals.veganacademy.server.features.routes.user.UserRegisterRequest

interface UserService {

    suspend fun login(request: UserLoginRequest): Response<UserDTO>
    suspend fun register(request: UserRegisterRequest): Response<UserDTO>
}