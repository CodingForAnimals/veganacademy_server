package org.codingforanimals.veganacademy.server.features.model.data.recipes

import com.google.gson.annotations.SerializedName

data class RecipesFilter(
    val category: String = "MAIN_DISH",
    val ingredients: List<String> = listOf(),
    val title: String = "",
    @SerializedName("get_accepted_recipes") val getAcceptedRecipes: Boolean = true,
    @SerializedName("is_order_asc") val isOrderAsc: Boolean = false,
    @SerializedName("order_by") val orderBy: String = RecipesOrderByEnum.LIKES.column.name,
)