package org.codingforanimals.veganacademy.server.features.model.repository.impl

import java.util.*
import org.codingforanimals.veganacademy.server.features.model.data.dto.RememberMeCredentialsDTO
import org.codingforanimals.veganacademy.server.features.model.data.source.RememberMeDataSource
import org.codingforanimals.veganacademy.server.features.model.mapper.toDto
import org.codingforanimals.veganacademy.server.features.model.repository.RememberMeRepository
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class RememberMeRepositoryImpl(
    private val dataSource: RememberMeDataSource
) : RememberMeRepository {

    override suspend fun getRememberMeCredentials(userId: Int, deviceUUID: String): RememberMeCredentialsDTO? {
        return newSuspendedTransaction { dataSource.getRememberMeCredentials(userId, deviceUUID)?.toDto() }
    }

    override suspend fun addRememberMeCredentials(userId: Int, deviceUUID: String): RememberMeCredentialsDTO? {
        val token = UUID.randomUUID().toString()
        return newSuspendedTransaction { dataSource.addLoggedInUser(userId, deviceUUID, token)?.toDto() }
    }

    override suspend fun validateRememberMeCredentials(dto: RememberMeCredentialsDTO): RememberMeCredentialsDTO? {
        return newSuspendedTransaction {
            dataSource.validateRememberMeCredentials(
                dto.userId,
                dto.userDeviceUUID,
                dto.userToken
            )?.toDto()
        }
    }
}