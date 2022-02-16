package org.codingforanimals.veganacademy.server.features.model.repository.impl

import org.codingforanimals.veganacademy.server.features.model.data.source.RecipeSource
import org.codingforanimals.veganacademy.server.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.server.features.model.mapper.toDto
import org.codingforanimals.veganacademy.server.features.model.mapper.toRecipeDtoList
import org.codingforanimals.veganacademy.server.features.model.repository.RecipeRepository
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationInfo
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationRequest
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationResponse
import org.codingforanimals.veganacademy.server.features.routes.recipes.RecipesFilter
import org.jetbrains.exposed.sql.Transaction
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

    override suspend fun getPaginatedRecipes(paginationRequest: PaginationRequest<RecipesFilter>): PaginationResponse<RecipesFilter, RecipeDTO> {
        return newSuspendedTransaction {
            var recipesDTO = getPaginatedRecipesByCategoriesOrIngredients(paginationRequest, this)

            val hasMoreContent = recipesDTO.size == paginationRequest.paginationInfo.pageSize + 1
            if (hasMoreContent) recipesDTO = recipesDTO.dropLast(1)

            return@newSuspendedTransaction createPaginationResponse(recipesDTO, paginationRequest, hasMoreContent)
        }
    }

    private fun getPaginatedRecipesByCategoriesOrIngredients(
        paginationRequest: PaginationRequest<RecipesFilter>,
        transaction: Transaction
    ): List<RecipeDTO> = with(paginationRequest) {
        return if (paginationRequest.filter.ingredients.isEmpty()) {
            source.getPaginatedRecipesByCategory(
                paginationInfo.pageSize,
                paginationInfo.pageNumber,
                filter
            )
        } else {
            source.getPaginatedRecipesByIngredients(
                paginationInfo.pageSize,
                paginationInfo.pageNumber,
                filter,
                transaction
            )
        }.toRecipeDtoList()
    }

    private fun createPaginationResponse(
        recipesDTO: List<RecipeDTO>,
        paginationRequest: PaginationRequest<RecipesFilter>,
        hasMoreContent: Boolean
    ): PaginationResponse<RecipesFilter, RecipeDTO> {
        val paginationInfo = PaginationInfo(
            pageSize = paginationRequest.paginationInfo.pageSize,
            pageNumber = paginationRequest.paginationInfo.pageNumber,
            hasMoreContent = hasMoreContent,
            resultSize = recipesDTO.size,
        )

        val filter = paginationRequest.filter

        return PaginationResponse(
            paginationInfo = paginationInfo,
            filter = filter,
            result = recipesDTO
        )
    }

    override suspend fun acceptRecipeById(id: Int): RecipeDTO? {
        return newSuspendedTransaction {
            source.acceptRecipeById(id).takeIf { it }?.let {
                source.findRecipeById(id)?.toDto()
            }
        }
    }
}