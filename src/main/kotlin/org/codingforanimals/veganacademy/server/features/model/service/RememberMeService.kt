package org.codingforanimals.veganacademy.server.features.model.service

import org.codingforanimals.veganacademy.server.features.model.dto.LoggedInUserDTO
import org.codingforanimals.veganacademy.server.features.routes.common.Response

interface RememberMeService {
    suspend fun getLoggedInUser(request: LoggedInUserDTO): Response<LoggedInUserDTO>
}