package org.codingforanimals.veganacademy.features.model.data.source.impl

import org.codingforanimals.veganacademy.features.model.dao.User
import org.codingforanimals.veganacademy.features.model.dao.UserTable
import org.codingforanimals.veganacademy.features.model.data.source.UserSource
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class UserSourceImpl : UserSource {

    override fun getUserById(id: Int): User? {
        val query = UserTable.select { UserTable.id eq id }
        return User.wrapRows(query).firstOrNull()
    }

    override fun getAllUsers(): SizedIterable<User> {
        val query = UserTable.selectAll()
        return User.wrapRows(query)
    }

    override fun createUser(email: String, displayName: String, passwordHash: String): User? {
        return try {
            User.new {
                this.email = email
                this.displayName = displayName
                this.passwordHash = passwordHash
            }
        } catch (e: Throwable) {
            null
        }
    }

    override fun findUserByEmail(email: String): User? {
        val user = UserTable.select { UserTable.email eq email }
        return User.wrapRows(user).firstOrNull()
    }
}