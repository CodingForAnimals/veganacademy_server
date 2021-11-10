package org.codingforanimals.routes

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.Route
import io.ktor.sessions.*
import org.codingforanimals.API_VERSION
import org.codingforanimals.auth.JwtService
import org.codingforanimals.auth.MySession
import org.codingforanimals.auth.hash
import org.codingforanimals.model.repository.UserRepository
import org.codingforanimals.model.repository.impl.UserRepositoryImpl
import org.koin.ktor.ext.inject

const val USERS = "$API_VERSION/users"
const val USER_LOGIN = "$USERS/login"
const val USER_CREATE = "$USERS/create"
const val USER_GET_ALL = "$USERS/all"

@KtorExperimentalLocationsAPI
@Location("users")
class UserRoutes {

    @Location("/authenticate")
    class Authenticate(val parent: UserRoutes)

    @Location("/register")
    class Register(val parent: UserRoutes)

    @Location("/all")
    class GetAll(val parent: UserRoutes)

}

@KtorExperimentalLocationsAPI
fun Route.userRoutes() {
    val userRepository by inject<UserRepositoryImpl>()
    val jwtService by inject<JwtService>()
    val hashFunction = { s: String -> hash(s) }

    authenticate("jwt") {
        get<UserRoutes.GetAll> {
            try {
                val users = userRepository.findAllUsers()
                call.respond(users)
            } catch (e: Throwable) {
                application.log.error("Failed to get all users", e)
                call.respond(HttpStatusCode.BadRequest,  "Error getting users")
            }
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
        val hash = hashFunction(password)
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
        val hash = hashFunction(password)
        try {
            val currentUser = userRepository.findUserByEmail(email)
            currentUser?.userId?.let {
                if (currentUser.passwordHash == hash) {
                    call.sessions.set(MySession(it))
                    call.respondText(jwtService.generateToken(currentUser))
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest, "Problems retrieving User"
                    )
                }
            }
        } catch (e: Throwable) {
            application.log.error("Failed to register user", e)
            call.respond(HttpStatusCode.BadRequest, "Problems retrieving User")
        }
    }
}


//@KtorExperimentalLocationsAPI
//@Location(USER_LOGIN)
//class UserLoginRoute
//
//@KtorExperimentalLocationsAPI
//@Location(USER_CREATE)
//class UserCreateRoute
//
//@KtorExperimentalLocationsAPI
//@Location(USER_GET_ALL)
//class UserGetAllRoute
//
//@KtorExperimentalLocationsAPI
//fun Route.users(
//    userRepository: UserRepository,
//    jwtService: JwtService,
//    hashFunction: (String) -> String
//) {
//    post<UserCreateRoute> {
//        val signupParameters = call.receive<Parameters>()
//        val password = signupParameters["password"]
//            ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing Fields")
//        val displayName = signupParameters["displayName"]
//            ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing Fields")
//        val email = signupParameters["email"]
//            ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing Fields")
//        val hash = hashFunction(password)
//        try {
//            val newUser = userRepository.addUser(email, displayName, hash)
//            newUser?.userId?.let {
//                call.sessions.set(MySession(it))
//                call.respondText(
//                    jwtService.generateToken(newUser),
//                    status = HttpStatusCode.Created
//                )
//            }
//        } catch (e: Throwable) {
//            application.log.error("Failed to register user", e)
//            call.respond(HttpStatusCode.BadRequest, "Problems creating User")
//        }
//    }
//
//    post<UserLoginRoute> {
//        val signInParameters = call.receive<Parameters>()
//        val password = signInParameters["password"]
//            ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing Fields")
//        val email = signInParameters["email"]
//            ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing Fields")
//        val hash = hashFunction(password)
//        try {
//            val currentUser = userRepository.findUserByEmail(email)
//            currentUser?.userId?.let {
//                if (currentUser.passwordHash == hash) {
//                    call.sessions.set(MySession(it))
//                    call.respondText(jwtService.generateToken(currentUser))
//                } else {
//                    call.respond(
//                        HttpStatusCode.BadRequest, "Problems retrieving User"
//                    )
//                }
//            }
//        } catch (e: Throwable) {
//            application.log.error("Failed to register user", e)
//            call.respond(HttpStatusCode.BadRequest, "Problems retrieving User")
//        }
//    }
//}