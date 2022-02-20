package org.codingforanimals.veganacademy.server.features.model.service.impl

import org.codingforanimals.veganacademy.server.features.model.data.dto.RecipeDTO
import org.codingforanimals.veganacademy.server.features.model.data.recipes.RecipesFilter
import org.codingforanimals.veganacademy.server.features.model.repository.RecipeRepository
import org.codingforanimals.veganacademy.server.features.model.service.RecipeService
import org.codingforanimals.veganacademy.server.features.model.service.RecipeService.Companion.MESSAGE_ACCEPT_FAILURE_DOES_NOT_EXIST
import org.codingforanimals.veganacademy.server.features.model.service.RecipeService.Companion.MESSAGE_ACCEPT_SUCCESS
import org.codingforanimals.veganacademy.server.features.model.service.RecipeService.Companion.MESSAGE_PAGINATION_SUCCESS
import org.codingforanimals.veganacademy.server.features.model.service.RecipeService.Companion.MESSAGE_SUGGEST_FAILURE
import org.codingforanimals.veganacademy.server.features.model.service.RecipeService.Companion.MESSAGE_SUGGEST_SUCCESS
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationRequest
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationResponse
import org.codingforanimals.veganacademy.server.features.routes.common.Response

class RecipeServiceImpl(
    private val recipeRepository: RecipeRepository,
) : RecipeService {

    override suspend fun suggestRecipe(recipeDTO: RecipeDTO): Response<RecipeDTO> {
        val addedRecipeDTO = recipeRepository.addRecipe(recipeDTO)
        return if (addedRecipeDTO == null) {
            Response.failure(MESSAGE_SUGGEST_FAILURE)
        } else {
            Response.success(MESSAGE_SUGGEST_SUCCESS, addedRecipeDTO)
        }
    }

    override suspend fun getPaginatedRecipes(request: PaginationRequest<RecipesFilter>): Response<PaginationResponse<RecipesFilter, RecipeDTO>> {
        return Response.success(MESSAGE_PAGINATION_SUCCESS, recipeRepository.getPaginatedRecipes(request))


    }

    override suspend fun acceptRecipeById(recipeId: Int): Response<RecipeDTO> {
        val recipeDTO = recipeRepository.acceptRecipeById(recipeId)
        return if (recipeDTO == null) {
            Response.failure(MESSAGE_ACCEPT_FAILURE_DOES_NOT_EXIST)
        } else {
            Response.success(MESSAGE_ACCEPT_SUCCESS, recipeDTO)
        }
    }
}