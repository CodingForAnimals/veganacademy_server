package org.codingforanimals.veganacademy.server.features.model.data.source

import org.codingforanimals.veganacademy.server.features.model.data.dao.RememberMeCredentials

interface RememberMeDataSource {
    fun getRememberMeCredentials(userId: Int, deviceUUID: String): RememberMeCredentials?
    fun addLoggedInUser(userId: Int, userDeviceUUID: String, userToken: String): RememberMeCredentials?
    fun validateRememberMeCredentials(userId: Int, userDeviceUUID: String, userToken: String): RememberMeCredentials?
}