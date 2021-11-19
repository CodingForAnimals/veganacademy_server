package org.codingforanimals.veganacademy.features.model.data.source

import org.codingforanimals.veganacademy.features.model.dao.User
import org.codingforanimals.veganacademy.features.model.dao.UserTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.insertIgnoreAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.sql.SQLIntegrityConstraintViolationException

class UserDataSourceImpl : UserDataSource {

    override fun getUserById(id: Int): User? {
        val query = UserTable.select { UserTable.id eq id }
        return User.wrapRows(query).firstOrNull()
    }

    override fun getAllUsers(): List<User> {
        val query = UserTable.selectAll()
        return User.wrapRows(query).toList()
    }

    override fun createUser(email: String, displayName: String, passwordHash: String): Int {
        try {
            val id = UserTable.insertAndGetId {
                it[UserTable.email] = email
                it[UserTable.displayName] = displayName
                it[UserTable.passwordHash] = passwordHash
            }
            return id.value
        } catch (e: ExposedSQLException) {
            println("error ${e}")
            throw e
        }
    }

    override fun getUserByEmail(email: String): User? {
        val user = UserTable.select { UserTable.email eq email }
        return User.wrapRows(user).firstOrNull()
    }
}