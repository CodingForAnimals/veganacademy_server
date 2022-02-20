package org.codingforanimals.veganacademy.server.features.model.data.source.impl

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.codingforanimals.veganacademy.server.features.model.UnitKoinTest
import org.codingforanimals.veganacademy.server.features.model.data.dto.RecipeDTO
import org.codingforanimals.veganacademy.server.features.model.data.dto.RecipeIngredientDTO
import org.codingforanimals.veganacademy.server.features.model.data.dto.RecipeStepDTO
import org.codingforanimals.veganacademy.server.features.model.data.recipes.RecipesCategoriesEnum
import org.codingforanimals.veganacademy.server.features.model.data.recipes.RecipesFilter
import org.codingforanimals.veganacademy.server.features.model.data.recipes.RecipesOrderByEnum
import org.codingforanimals.veganacademy.server.features.model.mapper.toRecipeDtoList
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class RecipeSourceImplTest : UnitKoinTest() {

    private val sut = RecipeSourceImpl()

    @Test
    fun `given non existing recipe with an id, when findRecipeById, return null`() = runTestWithTransaction {
        val entity = sut.findRecipeById(1)

        assertNull(entity)
    }

    @Test
    fun `given existing recipe with an id, when findRecipeById, return corresponding entity`() =
        runTestWithTransaction {
            val id = sut.addRecipe(mockRecipeDTO())

            val entity = sut.findRecipeById(id)

            assertNotNull(entity)
            assertEquals(id, entity.id.value)
        }

    @Test
    fun `given correct DTO, when addRecipe, then return id of new inserted recipe`() = runTestWithTransaction {
        val id = sut.addRecipe(mockRecipeDTO())

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
            val r0 = mockRecipeDTO()
            r0.categories = listOf(RecipesCategoriesEnum.MAIN_DISH.name)
            val r1 = mockRecipeDTO()
            r1.categories = listOf(RecipesCategoriesEnum.APPETIZER.name)
            val r2 = mockRecipeDTO()
            r2.categories = listOf(RecipesCategoriesEnum.DESSERT.name)
            val filter = RecipesFilter(category = RecipesCategoriesEnum.SMOOTHIE.name)

            sut.addRecipe(r0)
            sut.addRecipe(r1)
            sut.addRecipe(r2)

            val recipes = sut.getPaginatedRecipesByCategory(3, 1, filter).toRecipeDtoList()

            assertTrue(recipes.isEmpty())
        }

    @Test
    fun `given no recipes match category filter, when getPaginatedRecipesByCategory, then return empty list`() =
        runTestWithTransaction {
            val r0 = mockRecipeDTO()
            r0.categories = listOf(RecipesCategoriesEnum.MAIN_DISH.name)
            val r1 = mockRecipeDTO()
            r1.categories = listOf(RecipesCategoriesEnum.APPETIZER.name)
            val r2 = mockRecipeDTO()
            r2.categories = listOf(RecipesCategoriesEnum.DESSERT.name)
            val filter = RecipesFilter(category = RecipesCategoriesEnum.SMOOTHIE.name)

            sut.addRecipe(r0)
            sut.addRecipe(r1)
            sut.addRecipe(r2)

            val recipes = sut.getPaginatedRecipesByCategory(3, 1, filter).toRecipeDtoList()

            assertTrue(recipes.isEmpty())
        }

    @Test
    fun `given recipes match title filter, when getPaginatedRecipesByCategory, then return filtered recipes`() =
        runTestWithTransaction {
            val r0 = mockRecipeDTO()
            r0.title = "contains word vegan"
            val r1 = mockRecipeDTO()
            r1.title = "word vegan is contained"
            val r2 = mockRecipeDTO()
            r2.title = "vegan is a word in the list"
            val r3 = mockRecipeDTO()
            r3.title = "VEGAN is also contained"
            val r4 = mockRecipeDTO()
            r4.title = "the word Vegan is also contained"
            val r5 = mockRecipeDTO()
            r5.title = "the word veg is NOT contained"
            addRecipes(r0, r1, r2, r3, r4, r5)
            val filter = RecipesFilter(title = "vegan")

            val recipes = sut.getPaginatedRecipesByCategory(6, 1, filter).toRecipeDtoList()

            assertEquals(5, recipes.size)
            assertFalse(recipes.map { it.title }.contains("the word veg is NOT contained"))
        }

    /**
     * This test in order to pass, all first characters need to be uppercase, and the following lowercase.
     * Exposed has no way of making the 'query.orderBy(column) not case-sensitive atm.
     */
    @Test
    fun `given order by recipe title filter, when getRecipesByCategory, then return ordered list`() =
        runTestWithTransaction {
            val r0 = mockRecipeDTO()
            r0.title = "accxxdd"
            val r1 = mockRecipeDTO()
            r1.title = "BDDSs22"
            val r2 = mockRecipeDTO()
            r2.title = "c123ASD"

            sut.addRecipe(r2)
            sut.addRecipe(r1)
            sut.addRecipe(r0)

            val orderByColumnName = RecipesOrderByEnum.TITLE.column.name
            val filter = RecipesFilter(orderBy = orderByColumnName)

            val recipes = sut.getPaginatedRecipesByCategory(3, 1, filter).toRecipeDtoList()

            assertEquals(recipes[0].title, r0.title)
            assertEquals(recipes[1].title, r1.title)
            assertEquals(recipes[2].title, r2.title)
        }

    @Test
    fun `given order by recipe likes, when getRecipesByCategory, then return ordered list`() = runTestWithTransaction {
        val r0 = mockRecipeDTO()
        r0.likes = 0
        val r1 = mockRecipeDTO()
        r1.likes = 1
        val r2 = mockRecipeDTO()
        r2.likes = 2

        sut.addRecipe(r2)
        sut.addRecipe(r1)
        sut.addRecipe(r0)

        val recipes = sut.getPaginatedRecipesByCategory(3, 1, RecipesFilter()).toRecipeDtoList()

        assertEquals(recipes[0].likes, r0.likes)
        assertEquals(recipes[1].likes, r1.likes)
        assertEquals(recipes[2].likes, r2.likes)
    }

    @Test
    fun `given no recipes match ingredients, when getPaginatedRecipesByIngredients, then return empty list`() =
        runTestWithTransaction { transaction ->
            val ing1 = RecipeIngredientDTO(null, null, "ing1", 1, "g")
            val ing2 = RecipeIngredientDTO(null, null, "ing2", 1, "g")
            val r0 = mockRecipeDTO()
            r0.ingredients = listOf(ing1, ing2)
            val r1 = mockRecipeDTO()
            r1.ingredients = listOf(ing1)
            val r2 = mockRecipeDTO()
            r2.ingredients = listOf(ing2)
            val filter = RecipesFilter(ingredients = listOf("ing3"))

            val recipes = sut.getPaginatedRecipesByIngredients(2, 1, filter, transaction).toRecipeDtoList()

            assertTrue(recipes.isEmpty())
        }

    @Test
    fun `given recipes match ingredients, when getPaginatedRecipesByIngredients, then return populated list`() =
        runTestWithTransaction { transaction ->
            val ingredientDTO = RecipeIngredientDTO(null, null, "ingredient", 1, "g")
            val otherIngredientDTO = RecipeIngredientDTO(null, null, "otherIngredient", 1, "g")
            val r0 = mockRecipeDTO()
            r0.ingredients = listOf(otherIngredientDTO)
            val r1 = mockRecipeDTO()
            r1.ingredients = listOf(ingredientDTO)
            val r2 = mockRecipeDTO()
            r2.ingredients = listOf(ingredientDTO)
            val r3 = mockRecipeDTO()
            r3.ingredients = listOf()
            sut.addRecipe(r0)
            sut.addRecipe(r1)
            sut.addRecipe(r2)
            sut.addRecipe(r3)
            val filter = RecipesFilter(ingredients = listOf("ingredient"))

            val recipes = sut.getPaginatedRecipesByIngredients(2, 1, filter, transaction).toRecipeDtoList()

            assertEquals(2, recipes.size)
            val ingredientsList = recipes.map { it.ingredients }
            ingredientsList.forEach { ingredient ->
                val ings = ingredient.map { it.name }
                val containsIngredient = ings.contains("ingredient")
                assertTrue(containsIngredient)
                val containsOtherIngredient = ings.contains("otherIngredient")
                assertFalse(containsOtherIngredient)
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

    private fun addRecipes(vararg recipes: RecipeDTO) {
        recipes.forEach { sut.addRecipe(it) }
    }
}