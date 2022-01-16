package org.codingforanimals.veganacademy.server.features.model.repository.impl

import org.codingforanimals.veganacademy.server.features.model.data.dao.User
import org.codingforanimals.veganacademy.server.features.model.data.source.UserSource
import org.codingforanimals.veganacademy.server.features.model.dto.UserDTO
import org.codingforanimals.veganacademy.server.features.model.repository.UserRepository
import org.codingforanimals.veganacademy.server.features.model.mapper.toDto
import org.codingforanimals.veganacademy.server.features.model.mapper.toUsersDTO
import org.codingforanimals.veganacademy.server.utils.UserUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserRepositoryImpl(
    private val source: UserSource,
    private val userUtils: UserUtils
) : UserRepository {

    override suspend fun login(email: String, password: String): UserDTO? {
        return newSuspendedTransaction {
            source.findUserByEmail(email)
                .takeIf { it != null && userUtils.comparePasswords(password, it.passwordHash) }?.toDto()
        }
    }

    override suspend fun register(email: String, password: String, displayName: String): UserDTO? {
        return newSuspendedTransaction {
            val alreadyExistingUser = source.findUserByEmail(email)
            if (alreadyExistingUser != null) {
                null
            } else {
                source.createUser(email, userUtils.hashPassword(password), displayName)?.toDto()
            }
        }
    }

    override suspend fun findUserById(userId: Int): User? {
        return newSuspendedTransaction { source.getUserById(userId) }
    }

    override suspend fun findUserByEmail(email: String): UserDTO? {
        return newSuspendedTransaction { source.findUserByEmail(email)?.toDto() }
    }

    override suspend fun findAllUsers(): List<UserDTO?> {
        return newSuspendedTransaction { source.getAllUsers() }.toUsersDTO()
    }

    override suspend fun deleteAll(): Boolean {
        TODO("Not yet implemented")
    }

}