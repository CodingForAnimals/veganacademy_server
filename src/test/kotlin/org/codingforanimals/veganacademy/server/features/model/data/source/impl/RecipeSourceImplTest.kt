package org.codingforanimals.veganacademy.server.features.model.data.source.impl

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.codingforanimals.veganacademy.server.features.model.UnitKoinTest
import org.codingforanimals.veganacademy.server.features.model.data.source.RecipeSource
import org.codingforanimals.veganacademy.server.features.model.dto.RecipeIngredientDTO
//import org.codingforanimals.veganacademy.server.features.routes.recipes.RecipesFilter
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
    fun `given existing recipe with an id, when findRecipeById, return corresponding entity`() = runTestWithTransaction {
        val id = sut.addRecipe(recipeDTO)

        val entity = sut.findRecipeById(id!!)

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

//    @Test
//    fun `given useful filter, when getPaginatedRecipesByCategory`() = runTestWithTransaction {
//        val filter = RecipesFilter()
//        createRecipes(sut)
//
//        val recipes = sut.getPaginatedRecipesByCategory(
//            pageSize = 2,
//            pageNumber = 1,
//            filter = filter
//        )
//
//        assertFalse(recipes.empty())
//    }

    private suspend fun createRecipes(recipeSource: RecipeSource) {
        val r1 = recipeDTO

        val r2 = recipeDTO
        val r2ing = r2.ingredients.toMutableList()
        r2ing.add(RecipeIngredientDTO(null, null, "new ingredient", 3, "g"))
        r2.categories = listOf("DESSERT")
        r2.ingredients = r2ing

        val r3 = recipeDTO
        val r3cats = r3.categories.toMutableList()
        r3cats.add("DESSERT")
        r3cats.add("SOUP")
        r3.categories = r3cats

        val r4 = recipeDTO
        val r4ing = r4.ingredients.toMutableList()
        r4ing.add(RecipeIngredientDTO(null, null, "new ingredient", 3, "g"))
        r4.categories = listOf("DESSERT")
        r4.ingredients = r4ing

        recipeSource.addRecipe(r1)
        recipeSource.addRecipe(r2)
        recipeSource.addRecipe(r3)
        recipeSource.addRecipe(r4)
    }
}