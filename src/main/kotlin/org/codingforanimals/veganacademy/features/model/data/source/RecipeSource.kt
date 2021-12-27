package org.codingforanimals.veganacademy.features.model.data.source

import org.codingforanimals.veganacademy.features.model.dao.Recipe
import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeIngredientDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeStepDTO
import org.codingforanimals.veganacademy.features.routes.common.PaginationRequest
import org.codingforanimals.veganacademy.features.routes.common.PaginationResponse

interface RecipeSource {
    suspend fun findRecipeById(id: Int): Recipe?
    suspend fun addRecipe(recipeDTO: RecipeDTO): Recipe?
    suspend fun addRecipeIngredients(newRecipe: Recipe, ingredients: List<RecipeIngredientDTO>)
    suspend fun addRecipeSteps(newRecipe: Recipe, steps: List<RecipeStepDTO>)
    suspend fun getPaginatedRecipes(request: PaginationRequest): List<Recipe>
}