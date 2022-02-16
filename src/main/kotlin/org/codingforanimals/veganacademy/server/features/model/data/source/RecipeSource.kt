package org.codingforanimals.veganacademy.server.features.model.data.source

import org.codingforanimals.veganacademy.server.features.model.data.dao.Recipe
import org.codingforanimals.veganacademy.server.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.server.features.routes.recipes.RecipesFilter
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.Transaction

interface RecipeSource {
    fun findRecipeById(id: Int): Recipe?
    fun addRecipe(recipeDTO: RecipeDTO): Int?
    fun findRecipeByOffset(offset: Long): Recipe?
    fun acceptRecipeById(id: Int): Boolean
    fun getRecipesById(ids: List<Int>): SizedIterable<Recipe>
    fun getPaginatedRecipesByCategory(pageSize: Int, pageNumber: Int, filter: RecipesFilter): SizedIterable<Recipe>
    fun getPaginatedRecipesByIngredients(
        pageSize: Int,
        pageNumber: Int,
        filter: RecipesFilter,
        transaction: Transaction
    ): SizedIterable<Recipe>
}