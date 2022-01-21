package org.codingforanimals.veganacademy.server.features.model.service

import org.codingforanimals.veganacademy.server.features.model.dto.RememberMeCredentialsDTO
import org.codingforanimals.veganacademy.server.features.routes.common.Response

interface RememberMeService {
    suspend fun validateRememberMeCredentials(request: RememberMeCredentialsDTO): Response<RememberMeCredentialsDTO>
}