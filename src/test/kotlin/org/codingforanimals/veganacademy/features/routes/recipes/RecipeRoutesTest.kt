package org.codingforanimals.veganacademy.features.routes.recipes

import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.server.testing.*
import org.codingforanimals.veganacademy.features.model.dto.BaseRecipeIngredientDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeIngredientDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeStepDTO
import org.codingforanimals.veganacademy.features.routes.common.Response
import org.koin.test.AutoCloseKoinTest
import testutils.buildRequestBody
import testutils.gson
import testutils.setContentTypeText
import testutils.withTestServer
import org.junit.Test
import kotlin.test.assertTrue

@KtorExperimentalLocationsAPI
internal class RecipeRoutesTest : AutoCloseKoinTest() {

    private val recipeJSON = gson.toJson(
        RecipeDTO(
            title = "recipe title",
            description = "recipe description",
            categories = "cat1, cat2",
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