package org.codingforanimals.veganacademy.server.features.model.data.source.impl

//import org.codingforanimals.veganacademy.server.features.routes.recipes.RecipesFilter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.codingforanimals.veganacademy.server.features.model.UnitKoinTest
import org.codingforanimals.veganacademy.server.features.model.dto.RecipeIngredientDTO
import org.codingforanimals.veganacademy.server.features.routes.recipes.RecipesFilter
import org.junit.Test
import testutils.RecipeObjects
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class RecipeSourceImplTest : UnitKoinTest() {

    private val sut = RecipeSourceImpl()
    private val recipeDTO = RecipeObjects.recipeDTO

    @Test
    fun `given non existing recipe with an id, when findRecipeById, return null`() = runTestWithTransaction {
        val entity = sut.findRecipeById(1)

        assertNull(entity)
    }

    @Test
    fun `given existing recipe with an id, when findRecipeById, return corresponding entity`() =
        runTestWithTransaction {
            val id = sut.addRecipe(recipeDTO)

            val entity = sut.findRecipeById(id)

            assertNotNull(entity)
            assertEquals(id, entity.id.value)
        }

    @Test
    fun `given correct DTO, when addRecipe, then return id of new inserted recipe`() = runTestWithTransaction {
        val id = sut.addRecipe(recipeDTO)

        assertNotNull(id)
    }

    @Test
    fun `given empty list of ids, when getRecipesById, then return empty set`() = runTestWithTransaction {
        val recipes = sut.getRecipesById(listOf())

        assertTrue(recipes.empty())
    }

    @Test
    fun `given recipes match filter, when getPaginatedRecipesByCategory, then return populated list`() =
        runTestWithTransaction {
            val filter = RecipesFilter()
            createRecipes()

            val recipes = sut.getPaginatedRecipesByCategory(
                pageSize = 2,
                pageNumber = 1,
                filter = filter
            )

            assertFalse(recipes.empty())
        }

    @Test
    fun `given no recipes match filter, when getPaginatedRecipesByCategory, then return empty list`() =
        runTestWithTransaction {
            val filter = RecipesFilter(category = "SMOOTHIE")
            createRecipes()

            val recipes = sut.getPaginatedRecipesByCategory(
                pageSize = 2,
                pageNumber = 1,
                filter = filter
            )

            assertTrue(recipes.empty())
        }

    @Test
    fun `given no recipes match ingredients, when getPaginatedRecipesByIngredients, then return empty list`() =
        runTestWithTransaction { transaction ->
            val filter = RecipesFilter(
                ingredients = listOf("celery")
            )
            createRecipes()

            val recipes = sut.getPaginatedRecipesByIngredients(
                pageSize = 2,
                pageNumber = 1,
                filter = filter,
                transaction = transaction
            )

            assertTrue(recipes.empty())
        }

    @Test
    fun `given recipes match ingredients, when getPaginatedRecipesByIngredients, then return populated list`() =
        runTestWithTransaction { transaction ->
            val filter = RecipesFilter(ingredients = listOf("new ingredient"))
            createRecipes()

            val recipes = sut.getPaginatedRecipesByIngredients(
                pageSize = 2,
                pageNumber = 1,
                filter = filter,
                transaction = transaction
            )

            assertFalse(recipes.empty())

            val ingredientsList = recipes.map { it.ingredients }
            ingredientsList.forEach { iterable ->
                val ings = iterable.map { it.name }
                val contains = ings.contains("new ingredient")
                assertTrue(contains)
            }
        }

    private fun createRecipes() {
        sut.addRecipe(recipeDTO)

        val ing = recipeDTO.ingredients.toMutableList()
        ing.add(RecipeIngredientDTO(null, null, "new ingredient", 3, "g"))
        recipeDTO.categories = listOf("DESSERT", "MAIN_DISH")
        recipeDTO.ingredients = ing
        sut.addRecipe(recipeDTO)

        recipeDTO.categories = listOf("MAIN_DISH", "SOUP")
        sut.addRecipe(recipeDTO)
    }
}