package org.codingforanimals.veganacademy.server.features.model.repository

import org.codingforanimals.veganacademy.server.features.model.dto.LoggedInUserDTO

interface LoggedInUserRepository {
    suspend fun getLoggedInUser(dto: LoggedInUserDTO): LoggedInUserDTO?
    suspend fun addLoggedInUser(dto: LoggedInUserDTO): LoggedInUserDTO?
}