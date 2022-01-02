package org.codingforanimals.veganacademy.features.model.repository

import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.features.routes.common.PaginationRequest
import org.codingforanimals.veganacademy.features.routes.common.PaginationResponse
import org.codingforanimals.veganacademy.features.routes.recipes.RecipePaginationRequestFilter
import org.codingforanimals.veganacademy.features.routes.recipes.RecipePaginationResponseResult

interface RecipeRepository {
    suspend fun findRecipeById(id: Int): RecipeDTO?
    suspend fun addRecipe(recipeDTO: RecipeDTO): RecipeDTO?
    suspend fun getPaginatedRecipes(paginationRequest: PaginationRequest<RecipePaginationRequestFilter>): PaginationResponse<RecipePaginationResponseResult>
    suspend fun acceptRecipeById(id: Int): RecipeDTO?
}