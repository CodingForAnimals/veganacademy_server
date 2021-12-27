package org.codingforanimals.veganacademy.features.model.repository.impl

import org.codingforanimals.veganacademy.features.model.data.source.RecipeSource
import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.features.model.repository.RecipeRepository
import org.codingforanimals.veganacademy.features.model.repository.mapper.toDto
import org.codingforanimals.veganacademy.features.model.repository.mapper.toRecipeDtoList
import org.codingforanimals.veganacademy.features.routes.common.PaginationRequest
import org.codingforanimals.veganacademy.features.routes.common.PaginationResponse
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class RecipeRepositoryImpl(private val source: RecipeSource) : RecipeRepository {

    override suspend fun submitRecipe(recipeDTO: RecipeDTO): RecipeDTO? {
        return newSuspendedTransaction {
            val newRecipe = source.addRecipe(recipeDTO)
            newRecipe.takeIf { it != null }?.let {
                source.addRecipeIngredients(it, recipeDTO.ingredients)
                source.addRecipeSteps(it, recipeDTO.steps)
                source.findRecipeById(it.id.value)?.toDto()
            }
        }
    }

    override suspend fun findRecipeById(id: Int): RecipeDTO? {
        return newSuspendedTransaction {
            source.findRecipeById(id)?.toDto()
        }
    }

    override suspend fun getPaginatedRecipes(pageSize: Int, pageNumber: Int): PaginationResponse<RecipeDTO> {
        return newSuspendedTransaction {
            val request = PaginationRequest(pageSize, pageNumber)
            val recipes = source.getPaginatedRecipes(request)
            recipes.forEach {
                println(it.steps)
            }
            PaginationResponse(false, request.pageSize, request.pageNumber, recipes.toRecipeDtoList())
        }
    }
}