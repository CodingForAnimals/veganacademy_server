package org.codingforanimals.veganacademy.server.features.routes.user

import io.ktor.server.locations.KtorExperimentalLocationsAPI
import io.ktor.server.locations.Location


@KtorExperimentalLocationsAPI
@Location("users")
class UserLocations {

    @Location("/register")
    class Register(val parent: UserLocations)

    @Location("/login")
    class Login(val parent: UserLocations)

    @Location("/logout")
    class Logout(val parent: UserLocations)
}