package org.codingforanimals.veganacademy.server.features.routes.recipes

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.routing.Route
import org.codingforanimals.veganacademy.server.config.plugins.AUTH_SESSION
import org.codingforanimals.veganacademy.server.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.server.features.model.service.RecipeService
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationRequest
import org.codingforanimals.veganacademy.server.utils.errorResponse
import org.codingforanimals.veganacademy.server.utils.getRequest
import org.codingforanimals.veganacademy.server.utils.successResponse
import org.koin.ktor.ext.inject


@KtorExperimentalLocationsAPI
fun Route.recipeRoutes() {
    val recipeService by inject<RecipeService>()

    authenticate(AUTH_SESSION) {
        post<RecipeLocations.Suggest> {
            try {
                val request = call.getRequest<RecipeDTO>()
                val response = recipeService.suggestRecipe(request.content)
                call.successResponse(response)
            } catch (e: Throwable) {
                call.errorResponse(e)
            }
        }

        post<RecipeLocations.Paginated> {
            try {
                val request = call.getRequest<PaginationRequest<RecipesFilter>>()
                val response = recipeService.getPaginatedRecipes(request.content)
                call.successResponse(response)
            } catch (e: Throwable) {
                call.errorResponse(e)
            }
        }

        get<RecipeLocations.Accept> { params ->
            try {
                val acceptedRecipe = recipeService.acceptRecipeById(params.recipeId)
                call.successResponse(acceptedRecipe)
            } catch (e: Throwable) {
                call.errorResponse(e)
            }
        }
    }


}