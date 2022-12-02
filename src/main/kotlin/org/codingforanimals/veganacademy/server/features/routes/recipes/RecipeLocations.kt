package org.codingforanimals.veganacademy.server.features.routes.recipes

import io.ktor.server.locations.KtorExperimentalLocationsAPI
import io.ktor.server.locations.Location

@KtorExperimentalLocationsAPI
@Location("recipe")
class RecipeLocations {

    @Location("/paginated")
    data class Paginated(val parent: RecipeLocations)

    @Location("/suggest")
    data class Suggest(val parent: RecipeLocations)

    @Location("/accept/{recipeId}")
    data class Accept(val recipeId: Int, val parent: RecipeLocations)

}