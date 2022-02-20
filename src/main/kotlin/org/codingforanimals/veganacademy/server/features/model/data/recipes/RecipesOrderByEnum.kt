package org.codingforanimals.veganacademy.server.features.model.data.recipes

import org.codingforanimals.veganacademy.server.features.model.data.dao.RecipeTable
import org.jetbrains.exposed.sql.Column

enum class RecipesOrderByEnum(val column: Column<*>) {
    LIKES(RecipeTable.likes),
    TITLE(RecipeTable.title);

    companion object {
        fun getOrderByColumnName(name: String): Column<*>? {
            return when (name) {
                RecipeTable.likes.name -> RecipeTable.likes
                else -> null
            }
        }
    }
}