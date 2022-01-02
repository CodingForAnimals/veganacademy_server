package org.codingforanimals.veganacademy.features.model.repository.impl

import org.codingforanimals.veganacademy.features.model.data.source.RecipeSource
import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.features.model.repository.RecipeRepository
import org.codingforanimals.veganacademy.features.model.repository.mapper.toDto
import org.codingforanimals.veganacademy.features.model.repository.mapper.toRecipeDtoList
import org.codingforanimals.veganacademy.features.routes.common.PaginationRequest
import org.codingforanimals.veganacademy.features.routes.common.PaginationResponse
import org.codingforanimals.veganacademy.features.routes.recipes.RecipePaginationRequestFilter
import org.codingforanimals.veganacademy.features.routes.recipes.RecipePaginationResponseResult
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class RecipeRepositoryImpl(private val source: RecipeSource) : RecipeRepository {

    override suspend fun findRecipeById(id: Int): RecipeDTO? {
        return newSuspendedTransaction {
            source.findRecipeById(id)?.toDto()
        }
    }

    override suspend fun addRecipe(recipeDTO: RecipeDTO): RecipeDTO? {
        return newSuspendedTransaction {
            val newRecipeId = source.addRecipe(recipeDTO)
            val newRecipe = newRecipeId!!.let { source.findRecipeById(it) }
            newRecipe?.toDto()
        }
    }

    override suspend fun getPaginatedRecipes(paginationRequest: PaginationRequest<RecipePaginationRequestFilter>): PaginationResponse<RecipePaginationResponseResult> {
        return newSuspendedTransaction {
            var recipesDTO =
                source.getPaginatedRecipes(
                    paginationRequest.pageSize,
                    paginationRequest.pageNumber,
                    paginationRequest.filter,
                    this,
                ).toRecipeDtoList()

            val hasMoreContent = recipesDTO.size == paginationRequest.pageSize + 1
            if (hasMoreContent) recipesDTO = recipesDTO.dropLast(1)

            val result = RecipePaginationResponseResult(
                getAcceptedRecipes = paginationRequest.filter.getAcceptedRecipes,
                recipes = recipesDTO
            )
            PaginationResponse(
                hasMoreContent = hasMoreContent,
                resultSize = recipesDTO.size,
                pageSize = paginationRequest.pageSize,
                pageNumber = paginationRequest.pageNumber,
                result = result
            )
        }
    }

    override suspend fun acceptRecipeById(id: Int): RecipeDTO? {
        return newSuspendedTransaction {
            val recipe = source.acceptRecipeById(id)
            recipe?.toDto()
        }
    }
}