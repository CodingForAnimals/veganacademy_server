package org.codingforanimals.veganacademy.features.model.dto

data class RecipeDTO(
    var id: Int? = null,
    var name: String,
    var description: String,
    var categoriesId: String,
//    var cookingSteps: List<CookingStepDTO>,
//    var cookingIngredients: List<CookingIngredientDTO>,
//    var likes: Int,
)

data class CookingStepDTO(
    var id: Int? = null,
    var stepNumber: Int,
    var stepDescription: String,
)

data class CookingIngredientDTO(
    var id: Int? = null,
    var amount: Int,
    var measurementUnit: String,
    var name: String,
    var replacement: String? = null,
)