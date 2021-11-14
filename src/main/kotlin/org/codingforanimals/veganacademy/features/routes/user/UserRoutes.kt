package org.codingforanimals.veganacademy.features.routes.user

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.Route
import io.ktor.sessions.*
import org.codingforanimals.veganacademy.features.model.repository.UserRepository
import org.koin.ktor.ext.inject

@KtorExperimentalLocationsAPI
@Location("users")
class UserRoutes {

    @Location("/authenticate")
    class Authenticate(val parent: UserRoutes)

    @Location("/register")
    class Register(val parent: UserRoutes)

    @Location("/all")
    class GetAll(val parent: UserRoutes)

    @Location("/delete")
    class Delete(val parent: UserRoutes)

}

@KtorExperimentalLocationsAPI
fun Route.userRoutes() {
    val userRepository by inject<UserRepository>()
    val jwtService by inject<JwtService>()

    authenticate("jwt") {

    }

    get<UserRoutes.GetAll> {
        try {
            val users = userRepository.findAllUsers()
            call.respond(users)
        } catch (e: Throwable) {
            application.log.error("Failed to get all users", e)
            call.respond(HttpStatusCode.BadRequest, "Error getting users")
        }
    }

    get<UserRoutes.Delete> {
        try {
            val deleted = userRepository.deleteAll()
            call.respond(deleted)
        } catch (e: Throwable) {
            application.log.error("Error deleting users", e)
            call.respond(HttpStatusCode.InternalServerError, "Error deleting users")
        }
    }

    post<UserRoutes.Register> {
        val body = call.receive<Parameters>()
        val password = body["password"] ?: return@post respondMissingFields(call)
        val displayName = body["displayName"] ?: return@post respondMissingFields(call)
        val email = body["email"] ?: return@post respondMissingFields(call)
        val hash = jwtService.hash(password)
        try {
            val newUser = userRepository.addUser(email, displayName, hash)
            newUser?.userId?.let {
                call.sessions.set(MySession(it))
                call.respondText(
                    jwtService.generateToken(newUser),
                    status = HttpStatusCode.Created
                )
            }
        } catch (e: Throwable) {
            application.log.error("Failed to register user", e)
            call.respond(HttpStatusCode.BadRequest, "Problems creating User")
        }
    }

    post<UserRoutes.Authenticate> {
        val body = call.receive<Parameters>()
        val password = body["password"] ?: return@post respondMissingFields(call)
        val email = body["email"] ?: return@post respondMissingFields(call)
        try {
            val user = userRepository.findUserByEmail(email)!!
            if (jwtService.validate(password, user.passwordHash)) {
                call.sessions.set(MySession(user.userId))
                call.respondText(jwtService.generateToken(user))
            } else {
                respondLoginFailed(call)
            }
        } catch (e: Throwable) {
            application.log.error("Log in failed", e)
            respondLoginFailed(call)
        }
    }
}

private suspend fun respondMissingFields(call: ApplicationCall) {
    return call.respond(HttpStatusCode.BadRequest, MISSING_FIELDS_MESSAGE)
}

private suspend fun respondLoginFailed(call: ApplicationCall) {
    return call.respond(HttpStatusCode.BadRequest, "Log in failed")
}

private const val MISSING_FIELDS_MESSAGE = "Missing fields"