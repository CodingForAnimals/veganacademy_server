package org.codingforanimals.veganacademy.features.model.data.source

import org.codingforanimals.veganacademy.features.model.dao.Recipe
import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.features.routes.recipes.RecipePaginationRequestFilter
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.Transaction

interface RecipeSource {
    suspend fun findRecipeById(id: Int): Recipe?
    suspend fun addRecipe(recipeDTO: RecipeDTO): Int?
    suspend fun findRecipeByOffset(offset: Long): Recipe?
    suspend fun acceptRecipeById(id: Int): Recipe?
    suspend fun getPaginatedRecipes(
        pageSize: Int,
        pageNumber: Int,
        filter: RecipePaginationRequestFilter,
        transaction: Transaction,
    ): SizedIterable<Recipe>

    fun getRecipesById(ids: List<Int>): SizedIterable<Recipe>
}