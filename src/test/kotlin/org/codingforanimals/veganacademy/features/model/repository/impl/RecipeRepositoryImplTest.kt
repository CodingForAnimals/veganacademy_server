package org.codingforanimals.veganacademy.features.model.repository.impl

import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.codingforanimals.veganacademy.database.DatabaseFactoryForUnitTest
import org.codingforanimals.veganacademy.features.model.dao.Recipe
import org.codingforanimals.veganacademy.features.model.data.source.RecipeSource
import org.codingforanimals.veganacademy.features.model.dto.BaseRecipeIngredientDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeIngredientDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeStepDTO
import org.codingforanimals.veganacademy.features.model.repository.RecipeRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.get


@ExperimentalCoroutinesApi
class RecipeRepositoryImplTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(module {
            single<RecipeRepository> { RecipeRepositoryImpl(recipeSource) }
        })
    }

    private lateinit var databaseFactory: DatabaseFactoryForUnitTest

    private val recipeSource: RecipeSource = mockk()

    private lateinit var recipeRepository: RecipeRepository

    lateinit var recipe: Recipe

    private val recipeDTO = RecipeDTO(
        name = "recipe title",
        description = "recipe description",
        categoriesId = "cat1, cat2",
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
        recipeRepository = get()
    }

    @After
    fun tearDown() {
        databaseFactory.close()
    }

    @Test
    fun `given error, when submit recipe, don't delegate to source`() = runTest {
        every { recipeSource.addRecipe(any()) } returns null

        recipeRepository.submitRecipe(recipeDTO)

        verify(exactly = 0) {
            recipeSource.addRecipeIngredients(any(), any())
            recipeSource.addRecipeSteps(any(), any())
            recipeSource.findRecipeById(any())
        }
    }

    @Test
    fun `given success, when submit recipe, delegate to source`() = runTest {
        transaction {
            recipe = Recipe.new {
                name = "test recipe"
                description = "test description"
                categoriesId = "test categories"
                likes = 0
            }
        }
        every { recipeSource.addRecipe(any()) } returns recipe
        justRun { recipeSource.addRecipeIngredients(any(), any()) }
        justRun { recipeSource.addRecipeSteps(any(), any()) }
        every { recipeSource.findRecipeById(any()) } returns recipe

        recipeRepository.submitRecipe(recipeDTO)

        verify {
            recipeSource.addRecipe(any())
            recipeSource.addRecipeIngredients(any(), any())
            recipeSource.addRecipeSteps(any(), any())
            recipeSource.findRecipeById(any())
        }

    }

}