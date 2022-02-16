package org.codingforanimals.veganacademy.server.features.routes.recipes

import io.ktor.http.HttpMethod
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import org.codingforanimals.veganacademy.server.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.server.features.model.service.RecipeService
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationResponse
import org.codingforanimals.veganacademy.server.features.routes.common.Request
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import testutils.RecipeLocations.RecipeAcceptLocation
import testutils.RecipeLocations.RecipePaginatedLocation
import testutils.RecipeLocations.RecipeSuggestionLocation
import testutils.RecipeObjects.paginatedRecipesData
import testutils.RecipeObjects.recipeDTO
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
@RunWith(Enclosed::class)
internal class RecipeRoutesTest : AutoCloseKoinTest() {

    class SuggestRecipeRoutes {
        private val suggestRecipeRequest = Request(recipeDTO).toJson()

        @Test
        fun `given correct request, when suggesting a recipe, then return suggested recipe`() = withTestServer {
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
    }

    class AcceptRecipeRoutes {
        private val suggestRecipeRequest = Request(recipeDTO).toJson()

        @Test
        fun `given recipe is added, when accepting such recipe, then return accepted recipe`() = withTestServer {
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
    }

    class PaginatedRecipesRoutes {
        private val request = Request(paginatedRecipesData).toJson()

        @Test
        fun `given empty recipe result, when asking for paginated recipes, then return success but empty list`() =
            withTestServer {
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
                createRecipes {
                    handleRequest(HttpMethod.Post, RecipePaginatedLocation()) {
                        setBody(this@PaginatedRecipesRoutes.request)
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
            handleRequest(HttpMethod.Post, RecipeSuggestionLocation()) { setBody(Request(recipeDTO).toJson()) }.apply {
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
}