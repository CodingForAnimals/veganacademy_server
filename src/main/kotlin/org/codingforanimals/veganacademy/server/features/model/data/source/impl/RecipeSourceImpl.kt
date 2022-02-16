package org.codingforanimals.veganacademy.server.features.model.data.source.impl

import org.codingforanimals.veganacademy.server.features.model.data.dao.FoodCategory
import org.codingforanimals.veganacademy.server.features.model.data.dao.FoodCategoryTable
import org.codingforanimals.veganacademy.server.features.model.data.dao.Recipe
import org.codingforanimals.veganacademy.server.features.model.data.dao.RecipeFoodCategoryTable
import org.codingforanimals.veganacademy.server.features.model.data.dao.RecipeIngredient
import org.codingforanimals.veganacademy.server.features.model.data.dao.RecipeStep
import org.codingforanimals.veganacademy.server.features.model.data.dao.RecipeTable
import org.codingforanimals.veganacademy.server.features.model.data.source.RecipeSource
import org.codingforanimals.veganacademy.server.features.model.dto.BaseRecipeIngredientDTO
import org.codingforanimals.veganacademy.server.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.server.features.model.dto.RecipeIngredientDTO
import org.codingforanimals.veganacademy.server.features.model.dto.RecipeStepDTO
import org.codingforanimals.veganacademy.server.features.routes.recipes.RecipePaginationRequestFilter
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

    override suspend fun addRecipe(recipeDTO: RecipeDTO): Int {
        val newRecipe = createRecipe(recipeDTO)
        addRecipeAttributes(newRecipe, recipeDTO)
        return newRecipe.id.value
    }

    private fun addRecipeAttributes(newRecipe: Recipe, recipeDTO: RecipeDTO) {
        addRecipeIngredients(newRecipe, recipeDTO.ingredients)
        addRecipeSteps(newRecipe, recipeDTO.steps)
        addRecipeCategories(newRecipe, recipeDTO.categories)
    }

    private fun addRecipeIngredients(
        newRecipe: Recipe,
        ingredientsDTO: List<RecipeIngredientDTO>
    ) = ingredientsDTO.forEach { dto ->
        val ingredientReplacement = addIngredientReplacementIfNeeded(dto)
        addIngredient(newRecipe, dto, ingredientReplacement)
    }

    private fun addIngredientReplacementIfNeeded(ingredientDTO: RecipeIngredientDTO): RecipeIngredient? {
        var ingredientReplacement: RecipeIngredient? = null
        ingredientDTO.replacement?.let { replacement ->
            ingredientReplacement = addIngredientReplacement(replacement)
        }
        return ingredientReplacement
    }

    private fun addIngredientReplacement(replacement: BaseRecipeIngredientDTO): RecipeIngredient =
        RecipeIngredient.new {
            this.name = replacement.name
            this.quantity = replacement.quantity
            this.measurementUnit = replacement.measurementUnit
        }

    private fun addIngredient(
        newRecipe: Recipe,
        ingredientDTO: RecipeIngredientDTO,
        ingredientReplacement: RecipeIngredient?
    ) {
        RecipeIngredient.new {
            this.recipe = newRecipe
            this.name = ingredientDTO.name
            this.quantity = ingredientDTO.quantity
            this.measurementUnit = ingredientDTO.measurementUnit
            this.replacement = ingredientReplacement
        }
    }

    private fun addRecipeSteps(newRecipe: Recipe, steps: List<RecipeStepDTO>) {
        steps.forEach {
            RecipeStep.new {
                this.recipe = newRecipe
                this.number = it.number.toShort()
                this.description = it.description
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
        val limit = pageSize + 1
        val offset = ((pageNumber - 1) * pageSize)
        val categoryId = findCategoryByName(filter.category)?.id?.value
        val query =
            buildIngredientsPaginatedQuery(
                ingList,
                filter.getAcceptedRecipes,
                filter.ingredients.size,
                limit,
                offset,
                categoryId
            )
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

    override suspend fun acceptRecipeById(id: Int): Boolean {
        return RecipeTable.update({ RecipeTable.id eq id }) { it[isAccepted] = true } > 0
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

    private fun createRecipe(recipeDTO: RecipeDTO): Recipe =
        Recipe.new {
            this.title = recipeDTO.title
            this.description = recipeDTO.description
            this.likes = recipeDTO.likes
            this.isAccepted = recipeDTO.isAccepted
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

private fun buildIngredientsPaginatedQuery(
    ingredientsList: String,
    getAccepted: Boolean,
    ingredientsListSize: Int,
    limit: Int,
    offset: Int,
    categoryId: Int?,
): String {
    return if (categoryId != null) {
        "SELECT r.id FROM recipe as r " +
                "INNER JOIN recipeingredient as ri on ri.recipe = r.id " +
                "INNER JOIN recipefoodcategory as rfc on rfc.recipe = r.id " +
                "WHERE ri.name IN $ingredientsList " +
                "AND rfc.category = $categoryId " +
                "AND r.is_accepted = $getAccepted " +
                "GROUP BY r.id HAVING COUNT(*) = $ingredientsListSize " +
                "LIMIT $limit OFFSET $offset"
    } else {
        "SELECT r.id FROM recipe as r " +
                "INNER JOIN recipeingredient as ri on ri.recipe = r.id " +
                "WHERE ri.name IN $ingredientsList " +
                "AND r.is_accepted = $getAccepted " +
                "GROUP BY r.id HAVING COUNT(*) = $ingredientsListSize " +
                "LIMIT $limit OFFSET $offset"
    }
}