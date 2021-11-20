package org.codingforanimals.veganacademy.features.model.data.source

import org.codingforanimals.veganacademy.features.model.dao.User

interface UserSource {
    fun getUserById(id: Int): User?
    fun getAllUsers(): List<User>
    fun createUser(email: String, displayName: String, passwordHash: String): Int
    fun getUserByEmail(email: String): User?
}