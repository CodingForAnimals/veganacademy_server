package org.codingforanimals.veganacademy.server.features.model.service.impl

import org.codingforanimals.veganacademy.server.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.server.features.model.repository.RecipeRepository
import org.codingforanimals.veganacademy.server.features.model.service.RecipeService
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationRequest
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationResponse
import org.codingforanimals.veganacademy.server.features.routes.common.Response
import org.codingforanimals.veganacademy.server.features.routes.recipes.RecipePaginationRequestFilter
import org.codingforanimals.veganacademy.server.features.routes.recipes.RecipePaginationResponseResult

class RecipeServiceImpl(
    private val recipeRepository: RecipeRepository,
) : RecipeService {

    companion object {
        private const val MESSAGE_SUGGEST_FAILURE = "Suggest recipe failure"
        private const val MESSAGE_SUGGEST_SUCCESS = "Suggest recipe success"
        private const val MESSAGE_PAGINATION_SUCCESS = "Get paginated recipes success"
        private const val MESSAGE_ACCEPT_FAILURE = "Accept recipe failure"
        private const val MESSAGE_ACCEPT_SUCCESS = "Accept recipe success"
    }

    override suspend fun suggestRecipe(recipeDTO: RecipeDTO): Response<RecipeDTO> {
        val addedRecipeDTO = recipeRepository.addRecipe(recipeDTO)
        return if (addedRecipeDTO == null) {
            Response.failure(MESSAGE_SUGGEST_FAILURE)
        } else {
            Response.success(MESSAGE_SUGGEST_SUCCESS, addedRecipeDTO)
        }
    }

    override suspend fun getPaginatedRecipes(request: PaginationRequest<RecipePaginationRequestFilter>): Response<PaginationResponse<RecipePaginationResponseResult>> {
        return Response.success(MESSAGE_PAGINATION_SUCCESS, recipeRepository.getPaginatedRecipes(request))


    }

    override suspend fun acceptRecipeById(recipeId: Int): Response<RecipeDTO> {
        val recipeDTO = recipeRepository.acceptRecipeById(recipeId)
        return if (recipeDTO == null) {
            Response.failure(MESSAGE_ACCEPT_FAILURE)
        } else {
            Response.success(MESSAGE_ACCEPT_SUCCESS, recipeDTO)
        }
    }

}