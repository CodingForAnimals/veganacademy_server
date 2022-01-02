package org.codingforanimals.veganacademy.features.routes.recipes

import com.google.gson.annotations.SerializedName
import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO

data class RecipePaginationRequestFilter(
    @SerializedName("get_accepted_recipes") val getAcceptedRecipes: Boolean = true,
    @SerializedName("is_order_asc") val isOrderAsc: Boolean = false,
    val category: String = "",
    val name: String = "",
    val ingredients: List<String> = listOf(),
    @SerializedName("order_by") val orderBy: String = "",
)

data class RecipePaginationResponseResult(
    @SerializedName("get_accepted_recipes") val getAcceptedRecipes: Boolean,
    val recipes: List<RecipeDTO>,
)