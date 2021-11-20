package org.codingforanimals.veganacademy.features.routes.recipes

import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.server.testing.*
import org.codingforanimals.veganacademy.features.model.dto.CookingIngredientDTO
import org.codingforanimals.veganacademy.features.model.dto.CookingStepDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.features.routes.common.Response
import org.junit.Test
import org.koin.test.AutoCloseKoinTest
import testutils.gson
import testutils.withTestServer
import kotlin.test.assertTrue

@KtorExperimentalLocationsAPI
internal class RecipeRoutesTest : AutoCloseKoinTest() {

    private val recipeJSON = gson.toJson(
        RecipeDTO(
            name = "recipe title",
            description = "recipe description",
            categoriesId = listOf("category1", "category2"),
            cookingSteps = listOf(CookingStepDTO(stepNumber = 0, stepDescription = "cooking step description")),
            cookingIngredients = listOf(
                CookingIngredientDTO(
                    amount = 500,
                    measurementUnit = "g",
                    name = "ingredient",
                    replacement = "replacement if any"
                )
            ),
            likes = 0,
        )
    )

    private val submitRequest = mapOf("request" to recipeJSON).toString()

    @Test
    fun `given recipe request, when submitting, then return success`() = withTestServer {
        handleRequest(HttpMethod.Post, application.locations.href(RecipeRoutes.Submit(RecipeRoutes()))) {
            addHeader(HttpHeaders.ContentType, ContentType.Text.Any.toString())
            setBody(submitRequest)
        }.apply {
            val res = gson.fromJson(response.content!!, Response::class.java)
            assertTrue(res.success)
        }
    }
}