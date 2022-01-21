package org.codingforanimals.veganacademy.server.features.routes.recipes

import io.ktor.http.HttpMethod
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.locations
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import org.codingforanimals.veganacademy.server.features.model.dto.BaseRecipeIngredientDTO
import org.codingforanimals.veganacademy.server.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.server.features.model.dto.RecipeIngredientDTO
import org.codingforanimals.veganacademy.server.features.model.dto.RecipeStepDTO
import org.codingforanimals.veganacademy.server.features.routes.common.Response
import org.codingforanimals.veganacademy.server.utils.gson
import org.junit.Test
import org.koin.test.AutoCloseKoinTest
import testutils.buildRequestBody
import testutils.setContentTypeText
import testutils.withTestServer
import kotlin.test.assertTrue

@KtorExperimentalLocationsAPI
internal class RecipeRoutesTest : AutoCloseKoinTest() {

    private val recipeJSON = gson.toJson(
        RecipeDTO(
            title = "recipe title",
            description = "recipe description",
            categories = listOf("cat1", "cat2"),
            steps = listOf(RecipeStepDTO(number = 0, description = "cooking step description")),
            ingredients = listOf(
                RecipeIngredientDTO(
                    name = "ingredient",
                    quantity = 500,
                    measurementUnit = "g",
                    replacement = BaseRecipeIngredientDTO(
                        name = "replacement",
                        quantity = 250,
                        measurementUnit = "mg"
                    )
                )
            ),
            likes = 0,
        )
    )

    @Test
    fun `given recipe request, when submitting, then return success`() = withTestServer {
        handleRequest(HttpMethod.Post, application.locations.href(RecipeRoutes.Suggest(RecipeRoutes()))) {
            setContentTypeText(this)
            setBody(buildRequestBody(recipeJSON))
        }.apply {
            val res = gson.fromJson(response.content!!, Response::class.java)
            assertTrue(res.success)
        }
    }
}