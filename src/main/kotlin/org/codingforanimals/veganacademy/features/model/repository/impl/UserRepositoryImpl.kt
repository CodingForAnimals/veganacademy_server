package org.codingforanimals.veganacademy.features.model.repository.impl

import org.codingforanimals.veganacademy.features.model.dao.User
import org.codingforanimals.veganacademy.features.model.data.source.UserSource
import org.codingforanimals.veganacademy.features.model.repository.UserRepository
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserRepositoryImpl(private val source: UserSource) : UserRepository {

    override suspend fun addUser(email: String, displayName: String, passwordHash: String): User? {
        return newSuspendedTransaction {
            val user = source.findUserByEmail(email)
            return@newSuspendedTransaction if (user == null) {
                source.createUser(email, displayName, passwordHash)
            } else {
                null
            }
        }
    }

    override suspend fun findUserById(userId: Int): User? {
        return source.getUserById(userId)
    }

    override suspend fun findUserByEmail(email: String): User? {
        return newSuspendedTransaction { source.findUserByEmail(email) }
    }

    override suspend fun findAllUsers(): List<User?> {
        return newSuspendedTransaction { source.getAllUsers() }
    }

    override suspend fun deleteAll(): Boolean {
        TODO("Not yet implemented")
    }

}