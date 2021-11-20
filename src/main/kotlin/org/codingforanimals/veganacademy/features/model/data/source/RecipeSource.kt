package org.codingforanimals.veganacademy.features.model.data.source

import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO

interface RecipeSource {
    fun submitRecipe(recipeDTO: RecipeDTO): Int?
}