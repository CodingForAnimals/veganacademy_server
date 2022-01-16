package org.codingforanimals.veganacademy.server.features.model.data.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


object LoggedInUserTable : IntIdTable() {
    val userDeviceId = varchar("user_device_id", 128).uniqueIndex()
    val userToken = varchar("user_token", 128).uniqueIndex()
}

class LoggedInUser(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, LoggedInUser>(LoggedInUserTable)

    var userDeviceId by LoggedInUserTable.userDeviceId
    var userToken by LoggedInUserTable.userToken
}