package testutils

import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.locations
import io.ktor.server.testing.TestApplicationEngine
import org.codingforanimals.veganacademy.server.features.routes.user.UserLocations

@KtorExperimentalLocationsAPI
fun TestApplicationEngine.LoginLocation() = application.locations.href(UserLocations.Login(UserLocations()))

@KtorExperimentalLocationsAPI
fun TestApplicationEngine.RegisterLocation() = application.locations.href(UserLocations.Register(UserLocations()))