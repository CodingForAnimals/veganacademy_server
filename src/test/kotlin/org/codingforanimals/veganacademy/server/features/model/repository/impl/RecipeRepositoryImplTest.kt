package org.codingforanimals.veganacademy.server.features.model.repository.impl

import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.codingforanimals.veganacademy.server.database.DatabaseFactoryForUnitTest
import org.codingforanimals.veganacademy.server.features.model.dao.Recipe
import org.codingforanimals.veganacademy.server.features.model.data.source.RecipeSource
import org.codingforanimals.veganacademy.server.features.model.dto.BaseRecipeIngredientDTO
import org.codingforanimals.veganacademy.server.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.server.features.model.dto.RecipeIngredientDTO
import org.codingforanimals.veganacademy.server.features.model.dto.RecipeStepDTO
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.test.KoinTest

@ExperimentalCoroutinesApi
class RecipeRepositoryImplTest : KoinTest {

    private lateinit var databaseFactory: DatabaseFactoryForUnitTest

    private val recipeSource: RecipeSource = mockk()

    private val recipeRepository = RecipeRepositoryImpl(recipeSource)

    lateinit var recipe: Recipe

    private val recipeDTO = RecipeDTO(
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


    @Before
    fun setup() {
        databaseFactory = DatabaseFactoryForUnitTest()
        databaseFactory.connect()
    }

    @After
    fun tearDown() {
        databaseFactory.close()
    }

    @Test
    fun `given error, when submit recipe, don't delegate to source`() = runTest {
//        every { recipeSource.addRecipe(any()) } returns null
//
//        recipeRepository.addRecipe(recipeDTO)
//
//        verify(exactly = 0) {
//            recipeSource.addRecipeIngredients(any(), any())
//            recipeSource.addRecipeSteps(any(), any())
//            recipeSource.findRecipeById(any())
//        }
    }

    @Test
    fun `given success, when submit recipe, delegate to source`() = runTest {
        transaction {
            recipe = Recipe.new {
                title = "test recipe"
                description = "test description"
//                categories = listOf("cat1")
                likes = 0
            }
        }
//        every { recipeSource.addRecipe(any()) } returns recipe
//        justRun { recipeSource.addRecipeIngredients(any(), any()) }
//        justRun { recipeSource.addRecipeSteps(any(), any()) }
//        every { recipeSource.findRecipeById(any()) } returns recipe

        recipeRepository.addRecipe(recipeDTO)

        verify {
//            recipeSource.addRecipe(any())
//            recipeSource.addRecipeIngredients(any(), any())
//            recipeSource.addRecipeSteps(any(), any())
//            recipeSource.findRecipeById(any())
        }

    }

}