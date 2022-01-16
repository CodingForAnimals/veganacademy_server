package org.codingforanimals.veganacademy.server.features.model.repository.impl

import org.codingforanimals.veganacademy.server.features.model.data.source.LoggedInUserDataSource
import org.codingforanimals.veganacademy.server.features.model.dto.LoggedInUserDTO
import org.codingforanimals.veganacademy.server.features.model.repository.LoggedInUserRepository
import org.codingforanimals.veganacademy.server.features.model.mapper.toDto
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class LoggedInUserRepositoryImpl(
    private val dataSource: LoggedInUserDataSource
) : LoggedInUserRepository {
    override suspend fun getLoggedInUser(dto: LoggedInUserDTO): LoggedInUserDTO? {
        return newSuspendedTransaction { dataSource.getLoggedInUser(dto.userDeviceId, dto.userToken)?.toDto() }
    }

    override suspend fun addLoggedInUser(dto: LoggedInUserDTO): LoggedInUserDTO? {
        return newSuspendedTransaction { dataSource.addLoggedInUser(dto.userDeviceId, dto.userToken)?.toDto() }
    }
}