package org.codingforanimals.veganacademy.database

import org.codingforanimals.veganacademy.features.model.dao.FoodCategory
import org.codingforanimals.veganacademy.features.model.dao.FoodCategoryTable
import org.codingforanimals.veganacademy.features.model.dao.RecipeFoodCategoryTable
import org.codingforanimals.veganacademy.features.model.dao.RecipeIngredientTable
import org.codingforanimals.veganacademy.features.model.dao.RecipeStepTable
import org.codingforanimals.veganacademy.features.model.dao.RecipeTable
import org.codingforanimals.veganacademy.features.model.dao.UserTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object SchemaDefinition {

    fun createSchema() {
        transaction {
//            recreate()
        }
    }

    private fun recreate() {
        SchemaUtils.drop(UserTable, RecipeTable, RecipeStepTable, RecipeIngredientTable, FoodCategoryTable, RecipeFoodCategoryTable)
        SchemaUtils.create(UserTable, RecipeTable, RecipeStepTable, RecipeIngredientTable, FoodCategoryTable, RecipeFoodCategoryTable)

        FoodCategory.new {
            category = "BREAKFAST"
        }
        FoodCategory.new {
            category = "STARTER"
        }
        FoodCategory.new {
            category = "MAIN_DISH"
        }
        FoodCategory.new {
            category = "DESSERT"
        }
        FoodCategory.new {
            category = "SALAD"
        }
        FoodCategory.new {
            category = "SOUP"
        }
        FoodCategory.new {
            category = "APPETIZER"
        }
        FoodCategory.new {
            category = "SNACK"
        }
        FoodCategory.new {
            category = "DRINK"
        }
        FoodCategory.new {
            category = "SMOOTHIE"
        }
        FoodCategory.new {
            category = "GLUTEN_FREE"
        }
    }

}