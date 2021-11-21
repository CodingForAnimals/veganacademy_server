package org.codingforanimals.veganacademy.features.model.data.source

import org.codingforanimals.veganacademy.features.model.dao.Recipe
import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeIngredientDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeStepDTO

interface RecipeSource {
    fun findRecipeById(id: Int): Recipe?
    fun addRecipe(recipeDTO: RecipeDTO): Recipe?
    fun addRecipeIngredients(newRecipe: Recipe, ingredients: List<RecipeIngredientDTO>)
    fun addRecipeSteps(newRecipe: Recipe, steps: List<RecipeStepDTO>)
}