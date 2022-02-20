package org.codingforanimals.veganacademy.server.features.model.repository

import org.codingforanimals.veganacademy.server.features.model.data.dto.RecipeDTO
import org.codingforanimals.veganacademy.server.features.model.data.recipes.RecipesFilter
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationRequest
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationResponse

interface RecipeRepository {
    suspend fun findRecipeById(id: Int): RecipeDTO?
    suspend fun addRecipe(recipeDTO: RecipeDTO): RecipeDTO?
    suspend fun getPaginatedRecipes(paginationRequest: PaginationRequest<RecipesFilter>): PaginationResponse<RecipesFilter, RecipeDTO>
    suspend fun acceptRecipeById(id: Int): RecipeDTO?
}