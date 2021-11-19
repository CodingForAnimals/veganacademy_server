package org.codingforanimals.veganacademy.features.routes.user

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.Route
import io.ktor.sessions.*
import org.codingforanimals.veganacademy.features.model.repository.UserRepository
import org.codingforanimals.veganacademy.features.routes.common.Response
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

//    authenticate("jwt") {

    get<UserRoutes.GetAll> {

        try {
            val users = userRepository.findAllUsers()
            call.respond(users)
        } catch (e: Throwable) {
            application.log.error("Failed to get all users", e)
            call.respond(HttpStatusCode.BadRequest, "Error getting users")
        }
    }

//    }

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
        val email = body["email"] ?: return@post respondMissingFields(call)
        val password = body["password"] ?: return@post respondMissingFields(call)
        val displayName = body["displayName"] ?: return@post respondMissingFields(call)

        try {
            val user = userRepository.findUserByEmail(email)
            check(user == null)

            val hash = jwtService.hash(password)
            val newUser = userRepository.addUser(email, displayName, hash)!!

            val id = newUser.id.value
            val token = jwtService.generateToken(id)

            call.sessions.set(MySession(id))
            call.respond(Response.success(token, "User registered successfully"))
        } catch (e: Throwable) {
            application.log.error("Error in route ${call.request.uri}", e)
            call.respond(HttpStatusCode.InternalServerError, Response.failure<String>("User registration failed"))
        }
    }

    post<UserRoutes.Authenticate> {
        val body = call.receive<Parameters>()
        val password = body["password"] ?: return@post respondMissingFields(call)
        val email = body["email"] ?: return@post respondMissingFields(call)
        try {
            val user = userRepository.findUserByEmail(email)!!
            check(jwtService.validate(password, user.passwordHash)) { "Log in failed" }

            val id = user.id.value
            val token = jwtService.generateToken(id)

            call.sessions.set(MySession(id))
            call.respond(Response.success("User authenticated successfully", token))
        } catch (e: Throwable) {
            application.log.error("Error in route ${call.request.uri}", e)
            call.respond(HttpStatusCode.InternalServerError, Response.failure<String>("User authentication failed"))
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