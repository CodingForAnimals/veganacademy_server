package org.codingforanimals.veganacademy.database

import org.codingforanimals.veganacademy.features.model.dao.RecipeIngredientTable
import org.codingforanimals.veganacademy.features.model.dao.RecipeStepTable
import org.codingforanimals.veganacademy.features.model.dao.RecipeTable
import org.codingforanimals.veganacademy.features.model.dao.UserTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object SchemaDefinition {

    fun createSchema() {
        transaction {

//            SchemaUtils.drop(UserTable, RecipeTable, RecipeStepTable, RecipeIngredientTable)
//            SchemaUtils.create(UserTable, RecipeTable, RecipeStepTable, RecipeIngredientTable)
        }
    }

}