package org.codingforanimals.veganacademy.server.features.model.service

import org.codingforanimals.veganacademy.server.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationRequest
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationResponse
import org.codingforanimals.veganacademy.server.features.routes.common.Response
import org.codingforanimals.veganacademy.server.features.routes.recipes.RecipesFilter

interface RecipeService {

    suspend fun suggestRecipe(recipeDTO: RecipeDTO): Response<RecipeDTO>
    suspend fun getPaginatedRecipes(request: PaginationRequest<RecipesFilter>): Response<PaginationResponse<RecipesFilter, RecipeDTO>>
    suspend fun acceptRecipeById(recipeId: Int): Response<RecipeDTO>

    companion object {
        const val MESSAGE_SUGGEST_FAILURE = "Suggest recipe failure"
        const val MESSAGE_SUGGEST_SUCCESS = "Suggest recipe success"
        const val MESSAGE_PAGINATION_SUCCESS = "Get paginated recipes success"
        const val MESSAGE_ACCEPT_FAILURE_DOES_NOT_EXIST = "Accept recipe failure"
        const val MESSAGE_ACCEPT_SUCCESS = "Accept recipe success"
    }
}