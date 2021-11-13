package org.codingforanimals.veganacademy.routes

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.Route
import io.ktor.sessions.*
import org.codingforanimals.veganacademy.auth.JwtService
import org.codingforanimals.veganacademy.auth.MySession
import org.codingforanimals.veganacademy.model.repository.UserRepository
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
        val signupParameters = call.receive<Parameters>()
        val password = signupParameters["password"]
            ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing Fields")
        val displayName = signupParameters["displayName"]
            ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing Fields")
        val email = signupParameters["email"]
            ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing Fields")
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
        val signInParameters = call.receive<Parameters>()
        val password = signInParameters["password"]
            ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing Fields")
        val email = signInParameters["email"]
            ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing Fields")
        try {
            val user = userRepository.findUserByEmail(email)!!
            if (jwtService.validate(password, user.passwordHash)) {
                call.sessions.set(MySession(user.userId))
                call.respondText(jwtService.generateToken(user))
            } else {
                call.respond(
                    HttpStatusCode.BadRequest, "Log in failed"
                )
            }
        } catch (e: Throwable) {
            application.log.error("Log in failed", e)
            call.respond(HttpStatusCode.BadRequest, "Log in failed")
        }
    }
}