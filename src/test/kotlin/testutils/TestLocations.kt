@file:OptIn(KtorExperimentalLocationsAPI::class)

package testutils

import io.ktor.server.locations.KtorExperimentalLocationsAPI
import io.ktor.server.locations.locations
import io.ktor.server.testing.TestApplicationEngine
import org.codingforanimals.veganacademy.server.features.routes.recipes.RecipeLocations
import org.codingforanimals.veganacademy.server.features.routes.user.UserLocations
import org.codingforanimals.veganacademy.server.features.routes.user.rememberme.RememberMeLocations

object UserLocations {
    fun TestApplicationEngine.UserLoginLocation() = application.locations.href(UserLocations.Login(UserLocations()))

    fun TestApplicationEngine.UserLogoutLocation() =
        application.locations.href(UserLocations.Logout(UserLocations()))

    fun TestApplicationEngine.UserRegisterLocation() =
        application.locations.href(UserLocations.Register(UserLocations()))
}

object RecipeLocations {
    fun TestApplicationEngine.RecipeSuggestionLocation() =
        application.locations.href(RecipeLocations.Suggest(RecipeLocations()))

    fun TestApplicationEngine.RecipeAcceptLocation() =
        application.locations.href(RecipeLocations.Accept(1, RecipeLocations()))

    fun TestApplicationEngine.RecipePaginatedLocation() =
        application.locations.href(RecipeLocations.Paginated(RecipeLocations()))
}

object RememberMeLocations {
    fun TestApplicationEngine.RememberMeLocation() =
        application.locations.href(RememberMeLocations())
}
