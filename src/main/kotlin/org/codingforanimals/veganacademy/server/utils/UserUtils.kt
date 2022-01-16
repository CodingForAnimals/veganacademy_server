package org.codingforanimals.veganacademy.server.utils

import io.ktor.auth.Principal
import org.codingforanimals.veganacademy.server.config.plugins.UserSession
import org.codingforanimals.veganacademy.server.features.model.data.dao.User
import org.mindrot.jbcrypt.BCrypt

class UserUtils {

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun comparePasswords(password: String, hashedPassword: String?): Boolean {
        if (hashedPassword == null) return false
        return BCrypt.checkpw(password, hashedPassword)
    }

    fun createUserSession(user: User?): Principal? {
        if (user == null) return null
        return UserSession(
            userId = user.id.value
        )
    }

}