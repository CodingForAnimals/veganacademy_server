package org.codingforanimals.veganacademy.features.model.repository

import org.codingforanimals.veganacademy.features.model.dao.Recipe
import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO

interface RecipeRepository {
    suspend fun submitRecipe(recipeDTO: RecipeDTO): RecipeDTO?
    suspend fun findRecipeById(id: Int): RecipeDTO?
}