package org.codingforanimals.veganacademy.server.features.model.data.source.impl

import org.codingforanimals.veganacademy.server.features.model.data.dao.LoggedInUser
import org.codingforanimals.veganacademy.server.features.model.data.dao.LoggedInUserTable
import org.codingforanimals.veganacademy.server.features.model.data.source.LoggedInUserDataSource
import org.jetbrains.exposed.sql.and

class LoggedInUserDataSourceImpl : LoggedInUserDataSource {
    override fun getLoggedInUser(userDeviceId: String, userToken: String): LoggedInUser? {
        return LoggedInUser.find {
            (LoggedInUserTable.userDeviceId eq userDeviceId) and
                    (LoggedInUserTable.userToken eq userToken)
        }.firstOrNull()
    }

    override fun addLoggedInUser(userDeviceId: String, userToken: String): LoggedInUser? {
        return try {
            LoggedInUser.new {
                this.userDeviceId = userDeviceId
                this.userToken = userToken
            }
        } catch (e: Throwable) {
            null
        }
    }
}