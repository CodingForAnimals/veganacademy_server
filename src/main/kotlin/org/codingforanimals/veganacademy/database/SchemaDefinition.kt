package org.codingforanimals.veganacademy.database

import org.codingforanimals.veganacademy.features.model.dao.RecipeTable
import org.codingforanimals.veganacademy.features.model.dao.UserTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object SchemaDefinition {

    fun createSchema() {
        transaction {
            SchemaUtils.create(UserTable)
            SchemaUtils.create(RecipeTable)
        }
    }

}