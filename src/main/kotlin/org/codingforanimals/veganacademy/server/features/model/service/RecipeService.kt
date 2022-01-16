package org.codingforanimals.veganacademy.server.features.model.service

import org.codingforanimals.veganacademy.server.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationRequest
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationResponse
import org.codingforanimals.veganacademy.server.features.routes.common.Response
import org.codingforanimals.veganacademy.server.features.routes.recipes.RecipePaginationRequestFilter
import org.codingforanimals.veganacademy.server.features.routes.recipes.RecipePaginationResponseResult

interface RecipeService {

    suspend fun suggestRecipe(recipeDTO: RecipeDTO): Response<RecipeDTO>
    suspend fun getPaginatedRecipes(request: PaginationRequest<RecipePaginationRequestFilter>): Response<PaginationResponse<RecipePaginationResponseResult>>
    suspend fun acceptRecipeById(recipeId: Int): Response<RecipeDTO>
}