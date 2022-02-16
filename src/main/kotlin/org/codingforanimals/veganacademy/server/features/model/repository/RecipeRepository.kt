package org.codingforanimals.veganacademy.server.features.model.repository

import org.codingforanimals.veganacademy.server.features.model.data.dao.RecipeTable
import org.codingforanimals.veganacademy.server.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationRequest
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationResponse
import org.codingforanimals.veganacademy.server.features.routes.recipes.RecipesFilter
import org.jetbrains.exposed.sql.Column

interface RecipeRepository {
    suspend fun findRecipeById(id: Int): RecipeDTO?
    suspend fun addRecipe(recipeDTO: RecipeDTO): RecipeDTO?
    suspend fun getPaginatedRecipes(paginationRequest: PaginationRequest<RecipesFilter>): PaginationResponse<RecipesFilter, RecipeDTO>
    suspend fun acceptRecipeById(id: Int): RecipeDTO?
}

enum class RecipeOrderByEnum(val column: Column<*>) {
    TITLE(RecipeTable.title),
    LIKES(RecipeTable.likes)
}