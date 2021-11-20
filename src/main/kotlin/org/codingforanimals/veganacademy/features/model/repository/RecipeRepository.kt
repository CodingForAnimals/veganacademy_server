package org.codingforanimals.veganacademy.features.model.repository

import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO

interface RecipeRepository {
    suspend fun submitRecipe(recipeDTO: RecipeDTO): Int?
}