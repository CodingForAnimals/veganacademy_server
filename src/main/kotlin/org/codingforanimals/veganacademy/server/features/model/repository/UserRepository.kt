package org.codingforanimals.veganacademy.server.features.model.repository

import org.codingforanimals.veganacademy.server.features.model.data.dao.User
import org.codingforanimals.veganacademy.server.features.model.data.dto.UserDTO

interface UserRepository {
    suspend fun login(email: String, password: String): UserDTO?
    suspend fun register(email: String, password: String, displayName: String): UserDTO?
    suspend fun findUserById(userId: Int): User?
    suspend fun findUserByEmail(email: String): UserDTO?
    suspend fun findAllUsers(): List<UserDTO?>
    suspend fun deleteAll(): Boolean

}