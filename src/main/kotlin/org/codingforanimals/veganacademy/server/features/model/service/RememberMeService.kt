package org.codingforanimals.veganacademy.server.features.model.service

import org.codingforanimals.veganacademy.server.features.model.dto.RememberMeCredentialsDTO
import org.codingforanimals.veganacademy.server.features.routes.common.Response

interface RememberMeService {
    suspend fun validateRememberMeCredentials(request: RememberMeCredentialsDTO): Response<RememberMeCredentialsDTO>

    companion object {
        const val MESSAGE_LOGGED_IN_SUCCESS = "Remember Me Credentials fetched successfully"
        const val MESSAGE_LOGGED_IN_FAILURE = "Remember Me Credentials not found"
    }
}