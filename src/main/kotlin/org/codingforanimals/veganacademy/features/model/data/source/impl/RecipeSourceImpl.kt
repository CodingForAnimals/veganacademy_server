package org.codingforanimals.veganacademy.features.model.data.source.impl

import org.codingforanimals.veganacademy.features.model.dao.Recipe
import org.codingforanimals.veganacademy.features.model.dao.RecipeIngredient
import org.codingforanimals.veganacademy.features.model.dao.RecipeStep
import org.codingforanimals.veganacademy.features.model.dao.RecipeTable
import org.codingforanimals.veganacademy.features.model.data.source.RecipeSource
import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeIngredientDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeStepDTO
import org.codingforanimals.veganacademy.features.routes.common.PaginationRequest
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class RecipeSourceImpl : RecipeSource {

    override suspend fun findRecipeById(id: Int): Recipe? {
//        return newSuspendedTransaction {
           return Recipe.findById(id)
//        }
    }

    override suspend fun addRecipe(recipeDTO: RecipeDTO): Recipe? {
        return try {
//            newSuspendedTransaction {
                Recipe.new {
                    name = recipeDTO.name
                    description = recipeDTO.description
                    categoriesId = recipeDTO.categoriesId
                    likes = recipeDTO.likes
                }
//            }
        } catch (e: Throwable) {
            null
        }
    }

    override suspend fun addRecipeIngredients(newRecipe: Recipe, ingredients: List<RecipeIngredientDTO>) {
//        newSuspendedTransaction {
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
//        }
    }

    override suspend fun addRecipeSteps(newRecipe: Recipe, steps: List<RecipeStepDTO>) {
//        newSuspendedTransaction {
            steps.forEach {
                RecipeStep.new {
                    recipe = newRecipe
                    number = it.number.toShort()
                    description = it.description
                }
            }
//        }
    }

    override suspend fun getPaginatedRecipes(request: PaginationRequest): List<Recipe> {
        val pageNumber = request.pageNumber
        val pageSize = request.pageSize
        val offset = ((pageNumber - 1) * pageSize).toLong()
        return newSuspendedTransaction {
            val rows = Recipe.all().limit(pageSize, offset = offset)
            rows.toList()
        }
    }
}