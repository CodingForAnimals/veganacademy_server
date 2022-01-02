package org.codingforanimals.veganacademy.server.features.model.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object UserTable: IntIdTable() {
    val email = varchar("email", 128).uniqueIndex()
    val displayName = varchar("display_name", 256)
    val passwordHash = varchar("password_hash", 64)
}

class User(id: EntityID<Int>): Entity<Int>(id) {
    companion object: EntityClass<Int, User>(UserTable)

    var email by UserTable.email
    var displayName by UserTable.displayName
    var passwordHash by UserTable.passwordHash
}