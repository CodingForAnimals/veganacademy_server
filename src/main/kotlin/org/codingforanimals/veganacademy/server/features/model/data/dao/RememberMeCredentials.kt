package org.codingforanimals.veganacademy.server.features.model.data.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


object RememberMeCredentialsTable : IntIdTable() {
    val userId = integer("user_id")
    val userDeviceUUID = varchar("user_device_uuid", 128).uniqueIndex()
    val userToken = varchar("user_token", 128).uniqueIndex()
}

class RememberMeCredentials(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, RememberMeCredentials>(RememberMeCredentialsTable)

    var userId by RememberMeCredentialsTable.userId
    var userDeviceUUID by RememberMeCredentialsTable.userDeviceUUID
    var userToken by RememberMeCredentialsTable.userToken
}