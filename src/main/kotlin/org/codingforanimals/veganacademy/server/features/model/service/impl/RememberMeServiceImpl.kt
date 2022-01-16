package org.codingforanimals.veganacademy.server.features.model.service.impl

import org.codingforanimals.veganacademy.server.features.model.dto.LoggedInUserDTO
import org.codingforanimals.veganacademy.server.features.model.repository.LoggedInUserRepository
import org.codingforanimals.veganacademy.server.features.model.service.RememberMeService
import org.codingforanimals.veganacademy.server.features.routes.common.Response

class RememberMeServiceImpl(
    private val loggedInUserRepository: LoggedInUserRepository,
) : RememberMeService {

    companion object {
        private const val MESSAGE_LOGGED_IN_SUCCESS = "Logged in user remembered"
        private const val MESSAGE_LOGGED_IN_FAILURE = "Logged in user not remembered"
    }

    override suspend fun getLoggedInUser(request: LoggedInUserDTO): Response<LoggedInUserDTO> {
        val user = loggedInUserRepository.getLoggedInUser(request)
        return if (user == null) {
            Response.failure(MESSAGE_LOGGED_IN_FAILURE)
        } else {
            Response.success(MESSAGE_LOGGED_IN_SUCCESS, user)
        }
    }
}