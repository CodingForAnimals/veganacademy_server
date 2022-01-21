package org.codingforanimals.veganacademy.server.features.model.data.source.impl

import org.codingforanimals.veganacademy.server.features.model.data.dao.RememberMeCredentials
import org.codingforanimals.veganacademy.server.features.model.data.dao.RememberMeCredentialsTable
import org.codingforanimals.veganacademy.server.features.model.data.source.RememberMeDataSource
import org.jetbrains.exposed.sql.and

class RememberMeDataSourceImpl : RememberMeDataSource {

    override fun getRememberMeCredentials(userId: Int, deviceUUID: String): RememberMeCredentials? {
        return with(RememberMeCredentialsTable) {
            RememberMeCredentials.find {
                (this@with.userDeviceUUID eq deviceUUID) and
                        (this@with.userId eq userId)
            }.firstOrNull()
        }
    }

    override fun addLoggedInUser(userId: Int, userDeviceUUID: String, userToken: String): RememberMeCredentials? {
        return try {
            RememberMeCredentials.new {
                this.userId = userId
                this.userDeviceUUID = userDeviceUUID
                this.userToken = userToken
            }
        } catch (e: Throwable) {
            null
        }
    }

    override fun validateRememberMeCredentials(
        userId: Int,
        userDeviceUUID: String,
        userToken: String
    ): RememberMeCredentials? {
        return with(RememberMeCredentialsTable) {
            RememberMeCredentials.find {
                this@with.userId eq userId
                this@with.userDeviceUUID eq userDeviceUUID
                this@with.userToken eq userToken
            }.firstOrNull()
        }
    }
}