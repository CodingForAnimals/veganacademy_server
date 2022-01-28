package org.codingforanimals.veganacademy.server.features.model.repository.impl

import org.codingforanimals.veganacademy.server.features.model.data.source.RecipeSource
import org.codingforanimals.veganacademy.server.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.server.features.model.mapper.toDto
import org.codingforanimals.veganacademy.server.features.model.mapper.toRecipeDtoList
import org.codingforanimals.veganacademy.server.features.model.repository.RecipeRepository
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationRequest
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationResponse
import org.codingforanimals.veganacademy.server.features.routes.recipes.RecipePaginationRequestFilter
import org.codingforanimals.veganacademy.server.features.routes.recipes.RecipePaginationResponse
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class RecipeRepositoryImpl(private val source: RecipeSource) : RecipeRepository {

    override suspend fun findRecipeById(id: Int): RecipeDTO? {
        return newSuspendedTransaction {
            source.findRecipeById(id)?.toDto()
        }
    }

    override suspend fun addRecipe(recipeDTO: RecipeDTO): RecipeDTO? {
        return newSuspendedTransaction {
            source.addRecipe(recipeDTO)?.let {
                source.findRecipeById(it)?.toDto()
            }
        }
    }

    override suspend fun getPaginatedRecipes(paginationRequest: PaginationRequest<RecipePaginationRequestFilter>): PaginationResponse<RecipePaginationResponse> {
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

            val result = RecipePaginationResponse(
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
            source.acceptRecipeById(id).takeIf { it }?.let {
                source.findRecipeById(id)?.toDto()
            }
        }
    }
}