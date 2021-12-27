package org.codingforanimals.veganacademy.features.model.repository.mapper

import org.codingforanimals.veganacademy.features.model.dao.Recipe
import org.codingforanimals.veganacademy.features.model.dao.RecipeIngredient
import org.codingforanimals.veganacademy.features.model.dao.RecipeStep
import org.codingforanimals.veganacademy.features.model.dao.User
import org.codingforanimals.veganacademy.features.model.dto.BaseRecipeIngredientDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeIngredientDTO
import org.codingforanimals.veganacademy.features.model.dto.RecipeStepDTO
import org.codingforanimals.veganacademy.features.model.dto.UserDTO

fun Recipe.toDto() = RecipeDTO(
    id = id.value,
    name = name,
    description = description,
    categoriesId = categoriesId,
    steps = steps.map { it.toDto() },
    ingredients = ingredients.map { it.toDto() },
    likes = likes
)

fun List<Recipe>.toRecipeDtoList() = map { it.toDto() }

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

fun List<User?>.toUserDtoList() = map { it?.toDto() }