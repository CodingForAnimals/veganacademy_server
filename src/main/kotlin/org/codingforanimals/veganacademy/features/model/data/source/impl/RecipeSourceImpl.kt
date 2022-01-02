package org.codingforanimals.veganacademy.features.model.data.source.impl

import org.codingforanimals.veganacademy.features.model.dao.FoodCategory
import org.codingforanimals.veganacademy.features.model.dao.FoodCategoryTable
import org.codingforanimals.veganacademy.features.model.dao.Recipe
import org.codingforanimals.veganacademy.features.model.dao.RecipeFoodCategoryTable
import org.codingforanimals.veganacademy.features.model.dao.RecipeIngredient
import org.codingforanimals.veganacademy.features.model.dao.RecipeStep
import org.codingforanimals.veganacademy.features.model.dao.RecipeTable
import org.codingforanimals.veganacademy.features.model.data.source.RecipeSource
import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeIngredientDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeStepDTO
import org.codingforanimals.veganacademy.features.routes.recipes.RecipePaginationRequestFilter
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class RecipeSourceImpl : RecipeSource {

    override suspend fun findRecipeById(id: Int): Recipe? {
        return Recipe.findById(id)
    }

    override suspend fun addRecipe(recipeDTO: RecipeDTO): Int? {
        return try {
            val newRecipe = createRecipe(recipeDTO)
            newRecipe.takeIf { it != null }?.let {
                addRecipeIngredients(it, recipeDTO.ingredients)
                addRecipeSteps(it, recipeDTO.steps)
                addRecipeCategories(it, recipeDTO.categories)
                return it.id.value
            }
        } catch (e: Throwable) {
            null
        }
    }

    override suspend fun getPaginatedRecipes(
        pageSize: Int,
        pageNumber: Int,
        filter: RecipePaginationRequestFilter,
        transaction: Transaction,
    ): SizedIterable<Recipe> {
        return if (filter.ingredients.isEmpty()) {
            val query = createQueryWithFilter(filter)
            val offset = ((pageNumber - 1) * pageSize).toLong()
            query.limit(pageSize + 1, offset)
            Recipe.wrapRows(query)
        } else {
            getPaginatedRecipesByIngredients(transaction, pageSize, pageNumber, filter)
        }
    }

    private fun getPaginatedRecipesByIngredients(
        transaction: Transaction,
        pageSize: Int,
        pageNumber: Int,
        filter: RecipePaginationRequestFilter,
    ): SizedIterable<Recipe> {
        val ingList = filter.ingredients.joinToString("','", "('", "')")
        val offset = ((pageNumber - 1) * pageSize)
        val limit = pageSize + 1
        val query =
            "SELECT r.id FROM recipe as r INNER JOIN recipeingredient as ri on ri.recipe = r.id WHERE ri.name IN $ingList GROUP BY r.id HAVING COUNT(*) = ${filter.ingredients.size} LIMIT $limit OFFSET $offset"
        val ids = mutableListOf<Int>()
        transaction.exec(query) { rows ->
            while (rows.next()) {
                ids.add(rows.getInt("id"))
            }
        }
        val orderByPair = getOrderByPair(filter.orderBy, filter.isOrderAsc)
        return Recipe.forIds(ids).orderBy(orderByPair)
    }

    override fun getRecipesById(ids: List<Int>): SizedIterable<Recipe> {
        return Recipe.forIds(ids)
    }

    override suspend fun findRecipeByOffset(offset: Long): Recipe? {
        return Recipe.all().limit(1, offset).firstOrNull()
    }

    override suspend fun acceptRecipeById(id: Int): Recipe? {
        RecipeTable.update({ RecipeTable.id eq id }) { it[isAccepted] = true }
        val recipe = findRecipeById(id)
        return recipe.takeIf { it?.isAccepted == true }
    }

    private fun createQueryWithFilter(filter: RecipePaginationRequestFilter): Query {
        val category = findCategoryByName(filter.category)
        val categoryId = category?.id?.value

        val query = (RecipeTable innerJoin RecipeFoodCategoryTable).select {
            (RecipeTable.id eq RecipeFoodCategoryTable.recipe) and
                    (RecipeTable.isAccepted eq filter.getAcceptedRecipes) and
                    (RecipeFoodCategoryTable.category eq categoryId)
        }

        if (filter.name.isNotBlank()) {
            query.andWhere { RecipeTable.title like "%${filter.name}%" }
        }

        query.orderBy(RecipeTable.title)

        return query
    }

    private fun createRecipe(recipeDTO: RecipeDTO): Recipe? {
        return try {
            Recipe.new {
                title = recipeDTO.title
                description = recipeDTO.description
                likes = recipeDTO.likes
                isAccepted = recipeDTO.isAccepted
            }
        } catch (e: Throwable) {
            null
        }
    }


    private fun addRecipeIngredients(
        newRecipe: Recipe,
        ingredients: List<RecipeIngredientDTO>
    ) {
        ingredients.forEach { ingredient ->
            var ingredientReplacement: RecipeIngredient? = null
            ingredient.replacement?.let { replacement ->
                ingredientReplacement = RecipeIngredient.new {
                    name = replacement.name
                    quantity = replacement.quantity
                    measurementUnit = replacement.measurementUnit
                }
            }
            RecipeIngredient.new {
                recipe = newRecipe
                name = ingredient.name
                quantity = ingredient.quantity
                measurementUnit = ingredient.measurementUnit
                replacement = ingredientReplacement
            }
        }
    }

    private fun addRecipeSteps(newRecipe: Recipe, steps: List<RecipeStepDTO>) {
        steps.forEach {
            RecipeStep.new {
                recipe = newRecipe
                number = it.number.toShort()
                description = it.description
            }
        }
    }

    private fun addRecipeCategories(newRecipe: Recipe, categories: List<String>) {
        val availableCategories = mutableListOf<FoodCategory>()
        categories.forEach { categoryName ->
            findCategoryByName(categoryName)?.let { availableCategories.add(it) }
        }
        newRecipe.categories = SizedCollection(availableCategories)
    }

    private fun findCategoryByName(category: String): FoodCategory? {
        return FoodCategory.find { FoodCategoryTable.category eq category }.firstOrNull()
    }

    private fun getOrderByPair(orderBy: String, isOrderAsc: Boolean): Pair<Expression<*>, SortOrder> {
        val order = if (isOrderAsc) SortOrder.ASC else SortOrder.DESC
        return when (orderBy) {
            "TITLE" -> RecipeTable.title to order
            else -> RecipeTable.likes to order
        }
    }
}
