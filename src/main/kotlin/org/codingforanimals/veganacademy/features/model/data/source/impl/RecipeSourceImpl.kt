package org.codingforanimals.veganacademy.features.model.data.source.impl

import org.codingforanimals.veganacademy.features.model.dao.Recipe
import org.codingforanimals.veganacademy.features.model.dao.RecipeIngredient
import org.codingforanimals.veganacademy.features.model.dao.RecipeStep
import org.codingforanimals.veganacademy.features.model.data.source.RecipeSource
import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeIngredientDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeStepDTO

class RecipeSourceImpl : RecipeSource {

    override fun findRecipeById(id: Int): Recipe? {
        return Recipe.findById(id)
    }

    override fun addRecipe(recipeDTO: RecipeDTO): Recipe? {
        return try {
            Recipe.new {
                name = recipeDTO.name
                description = recipeDTO.description
                categoriesId = recipeDTO.categoriesId
                likes = recipeDTO.likes
            }
        } catch (e: Throwable) {
            null
        }
    }

    override fun addRecipeIngredients(newRecipe: Recipe, ingredients: List<RecipeIngredientDTO>) {
        ingredients.forEach { ingredient ->
            var ingredientReplacement: RecipeIngredient? = null
            ingredient.replacement?.let { replacement ->
                ingredientReplacement = RecipeIngredient.new {
                    name = replacement.name
                    quantity = replacement.quantity
                    measurementUnit = replacement.measurementUnit
                }
            }
            RecipeIngredient.new {
                recipe = newRecipe
                name = ingredient.name
                quantity = ingredient.quantity
                measurementUnit = ingredient.measurementUnit
                replacement = ingredientReplacement
            }
        }
    }

    override fun addRecipeSteps(newRecipe: Recipe, steps: List<RecipeStepDTO>) {
        steps.forEach {
            RecipeStep.new {
                recipe = newRecipe
                number = it.number.toShort()
                description = it.description
            }
        }
    }
}