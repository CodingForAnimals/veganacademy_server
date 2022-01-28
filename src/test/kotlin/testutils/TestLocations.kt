package testutils

import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.locations
import io.ktor.server.testing.TestApplicationEngine
import org.codingforanimals.veganacademy.server.features.routes.recipes.RecipeLocations
import org.codingforanimals.veganacademy.server.features.routes.user.UserLocations
import org.codingforanimals.veganacademy.server.features.routes.user.rememberme.RememberMeLocations

object UserLocations {
    @KtorExperimentalLocationsAPI
    fun TestApplicationEngine.UserLoginLocation() = application.locations.href(UserLocations.Login(UserLocations()))

    @KtorExperimentalLocationsAPI
    fun TestApplicationEngine.UserLogoutLocation() =
        application.locations.href(UserLocations.Logout(UserLocations()))

    @KtorExperimentalLocationsAPI
    fun TestApplicationEngine.UserRegisterLocation() =
        application.locations.href(UserLocations.Register(UserLocations()))
}

object RecipeLocations {
    @KtorExperimentalLocationsAPI
    fun TestApplicationEngine.RecipeSuggestionLocation() =
        application.locations.href(RecipeLocations.Suggest(RecipeLocations()))

    @KtorExperimentalLocationsAPI
    fun TestApplicationEngine.RecipeAcceptLocation() =
        application.locations.href(RecipeLocations.Accept(1, RecipeLocations()))

    @KtorExperimentalLocationsAPI
    fun TestApplicationEngine.RecipePaginatedLocation() =
        application.locations.href(RecipeLocations.Paginated(RecipeLocations()))
}

object RememberMeLocations {
    @KtorExperimentalLocationsAPI
    fun TestApplicationEngine.RememberMeLocation() =
        application.locations.href(RememberMeLocations())
}
