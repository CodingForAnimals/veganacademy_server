package org.codingforanimals.veganacademy.server.features.model.repository

import org.codingforanimals.veganacademy.server.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationRequest
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationResponse
import org.codingforanimals.veganacademy.server.features.routes.recipes.RecipePaginationRequestFilter
import org.codingforanimals.veganacademy.server.features.routes.recipes.RecipePaginationResponseResult

interface RecipeRepository {
    suspend fun findRecipeById(id: Int): RecipeDTO?
    suspend fun addRecipe(recipeDTO: RecipeDTO): RecipeDTO?
    suspend fun getPaginatedRecipes(paginationRequest: PaginationRequest<RecipePaginationRequestFilter>): PaginationResponse<RecipePaginationResponseResult>
    suspend fun acceptRecipeById(id: Int): RecipeDTO?
}