package org.codingforanimals.veganacademy.features.model.repository

import org.codingforanimals.veganacademy.features.model.dao.User
import org.codingforanimals.veganacademy.features.model.dto.UserDTO

interface UserRepository {

    suspend fun addUser(email: String, displayName: String, password: String): User?
    suspend fun findUserById(userId: Int): User?
    suspend fun findUserByEmail(email: String): User?
    suspend fun findAllUsers(): List<UserDTO?>
    suspend fun deleteAll(): Boolean

}