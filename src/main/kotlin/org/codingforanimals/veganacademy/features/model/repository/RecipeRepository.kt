package org.codingforanimals.veganacademy.features.model.repository

import org.codingforanimals.veganacademy.features.model.dao.Recipe
import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.features.routes.common.PaginationResponse

interface RecipeRepository {
    suspend fun submitRecipe(recipeDTO: RecipeDTO): RecipeDTO?
    suspend fun findRecipeById(id: Int): RecipeDTO?
    suspend fun getPaginatedRecipes(pageSize: Int, pageNumber: Int): PaginationResponse<RecipeDTO>
}