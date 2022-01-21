package org.codingforanimals.veganacademy.server.database

import org.codingforanimals.veganacademy.server.features.model.data.dao.FoodCategory
import org.codingforanimals.veganacademy.server.features.model.data.dao.FoodCategoryTable
import org.codingforanimals.veganacademy.server.features.model.data.dao.RememberMeCredentialsTable
import org.codingforanimals.veganacademy.server.features.model.data.dao.RecipeFoodCategoryTable
import org.codingforanimals.veganacademy.server.features.model.data.dao.RecipeIngredientTable
import org.codingforanimals.veganacademy.server.features.model.data.dao.RecipeStepTable
import org.codingforanimals.veganacademy.server.features.model.data.dao.RecipeTable
import org.codingforanimals.veganacademy.server.features.model.data.dao.UserTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object SchemaDefinition {

    private val tables = listOf(
        UserTable,
        RememberMeCredentialsTable,
        RecipeTable,
        RecipeStepTable,
        RecipeIngredientTable,
        FoodCategoryTable,
        RecipeFoodCategoryTable,
    ).toTypedArray()

    private val foodCategories = listOf(
        "BREAKFAST",
        "STARTER",
        "MAIN_DISH",
        "DESSERT",
        "SALAD",
        "SOUP",
        "APPETIZER",
        "SNACK",
        "DRINK",
        "SMOOTHIE",
        "GLUTEN_FREE",
    )

    fun createSchema() {
        transaction {
            recreate()
        }
    }

    private fun recreate() {
        recreateTables()
        recreateFoodCategories()
    }

    private fun recreateTables() {
        SchemaUtils.drop(*tables)
        SchemaUtils.create(*tables)
    }

    private fun recreateFoodCategories() =
        foodCategories.forEach {
            FoodCategory.new {
                this.category = it
            }
        }
}