package org.codingforanimals.veganacademy.server.features.routes.recipes

import com.google.gson.annotations.SerializedName
import org.codingforanimals.veganacademy.server.features.model.data.dao.RecipeTable
import org.jetbrains.exposed.sql.Column

data class RecipesFilter(
    val category: String = "MAIN_DISH",
    val ingredients: List<String> = listOf(),
    val name: String = "",
    @SerializedName("get_accepted_recipes") val getAcceptedRecipes: Boolean = true,
    @SerializedName("is_order_asc") val isOrderAsc: Boolean = false,
    @SerializedName("order_by") val orderBy: String = RecipeOrderByEnum.LIKES.column.name,
) {
    enum class RecipeOrderByEnum(val column: Column<*>) {
        LIKES(RecipeTable.likes),
        TITLE(RecipeTable.title)
    }
}