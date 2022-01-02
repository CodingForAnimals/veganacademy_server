package org.codingforanimals.veganacademy.server.features.model.data.source

import org.codingforanimals.veganacademy.server.features.model.dao.User
import org.jetbrains.exposed.sql.SizedIterable

interface UserSource {
    fun getUserById(id: Int): User?
    fun getAllUsers(): SizedIterable<User>
    fun createUser(email: String, displayName: String, passwordHash: String): User?
    fun findUserByEmail(email: String): User?
}