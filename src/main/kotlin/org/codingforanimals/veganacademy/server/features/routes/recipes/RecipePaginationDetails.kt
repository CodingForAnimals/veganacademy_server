package org.codingforanimals.veganacademy.server.features.routes.recipes

import com.google.gson.annotations.SerializedName
import org.codingforanimals.veganacademy.server.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.server.features.model.repository.RecipeOrderByEnum

data class RecipesFilter(
    val category: String = "",
    val ingredients: List<String> = listOf(),
    val name: String = "",
    @SerializedName("get_accepted_recipes") val getAcceptedRecipes: Boolean = true,
    @SerializedName("is_order_asc") val isOrderAsc: Boolean = false,
    @SerializedName("order_by") val orderBy: String = RecipeOrderByEnum.LIKES.column.name,
)