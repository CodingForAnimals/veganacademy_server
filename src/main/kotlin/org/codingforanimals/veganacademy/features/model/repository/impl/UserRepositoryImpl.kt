package org.codingforanimals.veganacademy.features.model.repository.impl

import org.codingforanimals.veganacademy.features.model.entity.User
import org.codingforanimals.veganacademy.features.model.entity.tables.Users
import org.codingforanimals.veganacademy.features.model.repository.UserRepository
import org.codingforanimals.veganacademy.db.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

class UserRepositoryImpl: UserRepository {

    override suspend fun addUser(email: String, displayName: String, passwordHash: String): User? {
        var statement: InsertStatement<Number>? = null
        dbQuery {
            statement = Users.insert { user ->
                user[Users.email] = email
                user[Users.displayName] = displayName
                user[Users.passwordHash] = passwordHash
            }
        }
        return rowToUser(statement?.resultedValues?.get(0))
    }

    override suspend fun findUserById(userId: Int): User? {
        return dbQuery { Users.select { Users.userId eq userId }.map { rowToUser(it) }.singleOrNull() }
    }

    override suspend fun findUserByEmail(email: String): User? {
        return dbQuery { Users.select { Users.email eq email }.map { rowToUser(it) }.singleOrNull() }
    }

    override suspend fun findAllUsers(): List<User?> {
        return dbQuery { Users.selectAll().map { rowToUser(it) } }
    }

    override suspend fun deleteAll(): Boolean {
        val res = dbQuery { Users.deleteAll() }
        return res != 0
    }

    private fun rowToUser(row: ResultRow?): User? {
        if (row == null) return null
        return User(
            userId = row[Users.userId],
            email = row[Users.email],
            displayName = row[Users.displayName],
            passwordHash = row[Users.passwordHash]
        )
    }
}