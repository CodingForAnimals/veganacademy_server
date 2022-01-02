package org.codingforanimals.veganacademy.server.features.routes.user

import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location


@KtorExperimentalLocationsAPI
@Location("users")
class UserLocations {

    @Location("/login")
    class Login(val parent: UserLocations)

    @Location("/logout")
    class Logout(val parent: UserLocations)

    @Location("/register")
    class Register(val parent: UserLocations)

    @Location("/all")
    class GetAll(val parent: UserLocations)

    @Location("/delete")
    class Delete(val parent: UserLocations)


}