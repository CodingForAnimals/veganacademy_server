package org.codingforanimals.veganacademy.features.model.repository.mapper

import org.codingforanimals.veganacademy.features.model.dao.FoodCategory
import org.codingforanimals.veganacademy.features.model.dao.Recipe
import org.codingforanimals.veganacademy.features.model.dao.RecipeIngredient
import org.codingforanimals.veganacademy.features.model.dao.RecipeStep
import org.codingforanimals.veganacademy.features.model.dao.User
import org.codingforanimals.veganacademy.features.model.dto.BaseRecipeIngredientDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeIngredientDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeStepDTO
import org.codingforanimals.veganacademy.features.model.dto.UserDTO
import org.jetbrains.exposed.sql.SizedIterable

fun Recipe.toDto() = RecipeDTO(
    id = id.value,
    title = title,
    description = description,
    categories = categories.toRecipeCategories(),
    steps = steps.toRecipeStepsDTO(),
    ingredients = ingredients.toRecipeIngredientsDTO(),
    likes = likes,
    isAccepted = isAccepted,
)

fun SizedIterable<Recipe>.toRecipeDtoList() = map { it.toDto() }

fun RecipeStep.toDto() = RecipeStepDTO(
    id = id.value,
    number = number.toInt(),
    description = description
)

fun RecipeIngredient.toDto() = RecipeIngredientDTO(
    id = id.value,
    name = name,
    quantity = quantity,
    measurementUnit = measurementUnit,
    replacement = replacement?.toBaseDto()
)

fun RecipeIngredient.toBaseDto() = BaseRecipeIngredientDTO(
    id = id.value,
    name = name,
    quantity = quantity,
    measurementUnit = measurementUnit
)

fun User.toDto() = UserDTO(
    userId = id.value,
    email = email,
    displayName = displayName
)

fun SizedIterable<RecipeStep>.toRecipeStepsDTO(): List<RecipeStepDTO> = map { it.toDto() }

fun SizedIterable<RecipeIngredient>.toRecipeIngredientsDTO(): List<RecipeIngredientDTO> = map { it.toDto() }

fun SizedIterable<User?>.toUsersDTO(): List<UserDTO?> = map { it?.toDto() }

fun SizedIterable<FoodCategory>.toRecipeCategories(): List<String> = map { it.category }