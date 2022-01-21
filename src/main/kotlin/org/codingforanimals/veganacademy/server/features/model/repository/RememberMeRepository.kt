package org.codingforanimals.veganacademy.server.features.model.repository

import org.codingforanimals.veganacademy.server.features.model.dto.RememberMeCredentialsDTO

interface RememberMeRepository {
    suspend fun getRememberMeCredentials(userId: Int,deviceUUID: String): RememberMeCredentialsDTO?
    suspend fun addRememberMeCredentials(userId: Int, deviceUUID: String): RememberMeCredentialsDTO?
    suspend fun validateRememberMeCredentials(dto: RememberMeCredentialsDTO): RememberMeCredentialsDTO?
}