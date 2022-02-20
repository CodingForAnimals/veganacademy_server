package org.codingforanimals.veganacademy.server.features.model.service.impl

import org.codingforanimals.veganacademy.server.features.model.data.dto.RememberMeCredentialsDTO
import org.codingforanimals.veganacademy.server.features.model.repository.RememberMeRepository
import org.codingforanimals.veganacademy.server.features.model.service.RememberMeService
import org.codingforanimals.veganacademy.server.features.model.service.RememberMeService.Companion.MESSAGE_LOGGED_IN_FAILURE
import org.codingforanimals.veganacademy.server.features.model.service.RememberMeService.Companion.MESSAGE_LOGGED_IN_SUCCESS
import org.codingforanimals.veganacademy.server.features.routes.common.Response

class RememberMeServiceImpl(
    private val rememberMeRepository: RememberMeRepository,
) : RememberMeService {

    override suspend fun validateRememberMeCredentials(request: RememberMeCredentialsDTO): Response<RememberMeCredentialsDTO> {
        val user = rememberMeRepository.validateRememberMeCredentials(request)
        return if (user == null) {
            Response.failure(MESSAGE_LOGGED_IN_FAILURE)
        } else {
            Response.success(MESSAGE_LOGGED_IN_SUCCESS, user)
        }
    }
}