package org.codingforanimals.veganacademy.server.features.routes.recipes

import io.ktor.http.HttpMethod
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import org.codingforanimals.veganacademy.server.features.model.data.dto.RecipeDTO
import org.codingforanimals.veganacademy.server.features.model.data.dto.RecipeIngredientDTO
import org.codingforanimals.veganacademy.server.features.model.data.dto.RecipeStepDTO
import org.codingforanimals.veganacademy.server.features.model.data.recipes.RecipesFilter
import org.codingforanimals.veganacademy.server.features.model.service.RecipeService
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationInfo
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationRequest
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationResponse
import org.codingforanimals.veganacademy.server.features.routes.common.Request
import org.junit.Test
import org.koin.test.AutoCloseKoinTest
import testutils.RecipeLocations.RecipeAcceptLocation
import testutils.RecipeLocations.RecipePaginatedLocation
import testutils.RecipeLocations.RecipeSuggestionLocation
import testutils.getParsedResponse
import testutils.setContentTypeFormUrlEncoded
import testutils.toJson
import testutils.withTestServer
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@KtorExperimentalLocationsAPI
internal class RecipeRoutesTest : AutoCloseKoinTest() {

    private val paginatedRecipesData = PaginationRequest(
        PaginationInfo(2, 2),
        RecipesFilter("MAIN_DISH"),
    )

    private val recipeDTO = mockRecipeDTO()

    @Test
    fun `given correct request, when suggesting a recipe, then return suggested recipe`() = withTestServer {
        val suggestRecipeRequest = Request(recipeDTO).toJson()

        handleRequest(HttpMethod.Post, RecipeSuggestionLocation()) {
            setContentTypeFormUrlEncoded()
            setBody(suggestRecipeRequest)
        }.apply {
            val res = getParsedResponse<RecipeDTO>()
            assertEquals(RecipeService.MESSAGE_SUGGEST_SUCCESS, res.message)
            assertNotNull(res.content)
            assertEquals(recipeDTO.title, res.content?.title)
            assertTrue(res.success)
        }
    }

    @Test
    fun `given recipe is added, when accepting such recipe, then return accepted recipe`() = withTestServer {
        val suggestRecipeRequest = Request(recipeDTO).toJson()
        handleRequest(HttpMethod.Post, RecipeSuggestionLocation()) {
            setContentTypeFormUrlEncoded()
            setBody(suggestRecipeRequest)
        }.apply {
            handleRequest(HttpMethod.Get, RecipeAcceptLocation()).apply {
                val res = getParsedResponse<RecipeDTO>()
                assertTrue(res.success)
                assertEquals(RecipeService.MESSAGE_ACCEPT_SUCCESS, res.message)
                assertEquals(true, res.content?.isAccepted)
            }
        }
    }

    @Test
    fun `given no recipe is added, when accepting a recipe, then return no recipe found`() = withTestServer {
        handleRequest(HttpMethod.Get, RecipeAcceptLocation()).apply {
            val res = getParsedResponse<Any>()
            assertNull(res.content)
            assertFalse(res.success)
            assertEquals(RecipeService.MESSAGE_ACCEPT_FAILURE_DOES_NOT_EXIST, res.message)
        }
    }

    @Test
    fun `given empty recipe result, when asking for paginated recipes, then return success but empty list`() =
        withTestServer {
            val request = Request(paginatedRecipesData).toJson()
            handleRequest(HttpMethod.Post, RecipePaginatedLocation()) {
                setBody(request)
            }.apply {
                val res = getParsedResponse<PaginationResponse<RecipesFilter, RecipeDTO>>()
                val pagination = res.content
                assertNotNull(res.content)
                assertEquals(RecipeService.MESSAGE_PAGINATION_SUCCESS, res.message)
                assertEquals(0, pagination?.paginationInfo?.resultSize)
                assertEquals(paginatedRecipesData.paginationInfo.pageSize, pagination?.paginationInfo?.pageSize)
                assertEquals(true, pagination?.result?.isEmpty())
            }
        }

    @Test
    fun `given success query with more recipes than requested, when asking for paginated recipes, then return success with data`() =
        withTestServer {
            val request = Request(paginatedRecipesData).toJson()
            createRecipes {
                handleRequest(HttpMethod.Post, RecipePaginatedLocation()) {
                    setBody(request)
                }.apply {
                    val res = getParsedResponse<PaginationResponse<RecipesFilter, RecipeDTO>>()
                    assertNotNull(res.content)
                    assertEquals(RecipeService.MESSAGE_PAGINATION_SUCCESS, res.message)
                    assertEquals(true, res.content?.paginationInfo?.hasMoreContent)
                    assertEquals(
                        paginatedRecipesData.paginationInfo.pageSize,
                        res.content?.paginationInfo?.resultSize
                    )
                }
            }
        }

    private fun TestApplicationEngine.createRecipes(callback: () -> Unit) {
        handleRequest(
            HttpMethod.Post,
            RecipeSuggestionLocation()
        ) { setBody(Request(recipeDTO).toJson()) }.apply {
            handleRequest(
                HttpMethod.Post,
                RecipeSuggestionLocation()
            ) { setBody(Request(recipeDTO).toJson()) }.apply {
                handleRequest(
                    HttpMethod.Post,
                    RecipeSuggestionLocation()
                ) { setBody(Request(recipeDTO).toJson()) }.apply {
                    handleRequest(
                        HttpMethod.Post,
                        RecipeSuggestionLocation()
                    ) { setBody(Request(recipeDTO).toJson()) }.apply {
                        handleRequest(
                            HttpMethod.Post,
                            RecipeSuggestionLocation()
                        ) { setBody(Request(recipeDTO).toJson()) }.apply {
                            callback()
                        }
                    }
                }
            }
        }
    }

    private fun mockRecipeDTO(): RecipeDTO {
        val recipeStepDTO = RecipeStepDTO(
            number = 1,
            description = "this is step number 1"
        )

        val recipeIngredientDTO = RecipeIngredientDTO(
            replacement = null,
            name = "recipe ingredient",
            quantity = 1,
            measurementUnit = "g"
        )

        return RecipeDTO(
            title = "recipe title",
            description = "recipe description",
            categories = listOf("MAIN_DISH"),
            steps = listOf(recipeStepDTO),
            ingredients = listOf(recipeIngredientDTO),
            likes = 1,
            isAccepted = true,
        )
    }
}
