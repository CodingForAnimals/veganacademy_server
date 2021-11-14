package org.codingforanimals.veganacademy.features.model.repository

import org.codingforanimals.veganacademy.features.model.entity.User

interface UserRepository {

    suspend fun addUser(email: String, displayName: String, passwordHash: String): User?
    suspend fun findUserById(userId: Int): User?
    suspend fun findUserByEmail(email: String): User?
    suspend fun findAllUsers(): List<User?>
    suspend fun deleteAll(): Boolean

}