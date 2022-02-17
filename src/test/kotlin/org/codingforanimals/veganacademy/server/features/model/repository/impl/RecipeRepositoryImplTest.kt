package org.codingforanimals.veganacademy.server.features.model.repository.impl

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.codingforanimals.veganacademy.server.database.DatabaseFactoryForUnitTest
import org.codingforanimals.veganacademy.server.features.model.data.dao.Recipe
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
import kotlin.test.assertNotNull

@ExperimentalCoroutinesApi
class RecipeRepositoryImplTest : KoinTest {

    private lateinit var databaseFactory: DatabaseFactoryForUnitTest

    private val recipeSource: RecipeSource = mockk()
    private val recipeMock = mockk<Recipe>()

    private val sut = RecipeRepositoryImpl(recipeSource)

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
    fun `given recipe saved successfully, when addRecipe, then return recipe`() = runTest {
        val insertedRecipe = insertRecipe()
        every { recipeSource.addRecipe(any()) } returns insertedRecipe.id.value
        every { recipeSource.findRecipeById(any()) } returns insertedRecipe

        val recipe = sut.addRecipe(recipeDTO)

        assertNotNull(recipe)
    }

    private fun insertRecipe() = transaction { Recipe.new {
        title = "title"
        description = "desc"
        likes = 1
        isAccepted = false
    } }

}