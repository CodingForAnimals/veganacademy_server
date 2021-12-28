package org.codingforanimals.veganacademy.features.model.dto

data class RecipeDTO(
    var id: Int? = null,
    var name: String,
    var description: String,
    var categoriesId: String,
    var steps: List<RecipeStepDTO>,
    var ingredients: List<RecipeIngredientDTO>,
    var likes: Int,
    var isAccepted: Boolean = false,
)

data class RecipeStepDTO(
    var id: Int? = null,
    var number: Int,
    var description: String,
)

class RecipeIngredientDTO(
    var replacement: BaseRecipeIngredientDTO?,
    id: Int? = null,
    name: String,
    quantity: Int,
    measurementUnit: String
) : BaseRecipeIngredientDTO(id, name, quantity, measurementUnit)

open class BaseRecipeIngredientDTO(
    var id: Int? = null,
    var name: String,
    var quantity: Int,
    var measurementUnit: String,
)