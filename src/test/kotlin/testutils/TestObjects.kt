package testutils

import org.codingforanimals.veganacademy.server.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.server.features.model.dto.RecipeIngredientDTO
import org.codingforanimals.veganacademy.server.features.model.dto.RecipeStepDTO
import org.codingforanimals.veganacademy.server.features.model.dto.RememberMeCredentialsDTO
import org.codingforanimals.veganacademy.server.features.routes.common.PaginationRequest
import org.codingforanimals.veganacademy.server.features.routes.common.Request
import org.codingforanimals.veganacademy.server.features.routes.recipes.RecipePaginationRequestFilter
import org.codingforanimals.veganacademy.server.features.routes.user.UserLoginRequest
import org.codingforanimals.veganacademy.server.features.routes.user.UserRegisterRequest

object UserObjects {
    val userLoginData = UserLoginRequest("uuid", "email123", "password123")
    val loginRequest = Request(userLoginData).toJson()

    val userRegisterData = UserRegisterRequest("uuid", userLoginData.email, userLoginData.password, "name")
    val registerRequest = Request(userRegisterData).toJson()
}

object RememberMeObjects {
    val rememberMeCredentials = RememberMeCredentialsDTO(
        userId = 1,
        userDeviceUUID = "user_device_UUID",
        userToken = "user_token",
    )

    val rememberMeRequest = Request(rememberMeCredentials).toJson()
}

object RecipeObjects {
    private val recipeStepDTO = RecipeStepDTO(
        number = 1,
        description = "this is step number 1"
    )

    private val recipeIngredientDTO = RecipeIngredientDTO(
        replacement = null,
        name = "recipe ingredient",
        quantity = 1,
        measurementUnit = "g"
    )

    val recipeDTO = RecipeDTO(
        title = "recipe title",
        description = "recipe description",
        categories = listOf("MAIN_DISH"),
        steps = listOf(recipeStepDTO),
        ingredients = listOf(recipeIngredientDTO),
        likes = 1,
        isAccepted = false,
    )

    private val paginationFilter = RecipePaginationRequestFilter(
        getAcceptedRecipes = false,
        category = "MAIN_DISH",
    )

    val paginatedRecipesData = PaginationRequest(
        pageSize = 2,
        pageNumber = 0,
        filter = paginationFilter,
    )
}

