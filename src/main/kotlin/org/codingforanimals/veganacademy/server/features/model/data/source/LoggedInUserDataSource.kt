package org.codingforanimals.veganacademy.server.features.model.data.source

import org.codingforanimals.veganacademy.server.features.model.data.dao.LoggedInUser

interface LoggedInUserDataSource {
    fun getLoggedInUser(userDeviceId: String, userToken: String): LoggedInUser?
    fun addLoggedInUser(userDeviceId: String, userToken: String): LoggedInUser?
}