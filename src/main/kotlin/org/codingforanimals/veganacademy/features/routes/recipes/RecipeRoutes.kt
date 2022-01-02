package org.codingforanimals.veganacademy.features.routes.recipes

import com.google.gson.Gson
import io.ktor.application.application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.auth.authenticate
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import org.codingforanimals.veganacademy.config.plugins.AUTH_SESSION
import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.features.model.repository.RecipeRepository
import org.codingforanimals.veganacademy.features.routes.common.PaginationRequest
import org.codingforanimals.veganacademy.features.routes.common.Request
import org.codingforanimals.veganacademy.features.routes.common.Response
import org.codingforanimals.veganacademy.features.routes.common.respondWithFailure
import org.codingforanimals.veganacademy.utils.genericType
import org.koin.ktor.ext.inject


@KtorExperimentalLocationsAPI
@Location("recipe")
class RecipeRoutes {

    @Location("/paginated")
    data class Paginated(val parent: RecipeRoutes)

    @Location("/suggest")
    data class Suggest(val parent: RecipeRoutes)

    @Location("/accept/{recipeId}")
    data class Accept(val recipeId: Int, val parent: RecipeRoutes)

}

@KtorExperimentalLocationsAPI
fun Route.recipeRoutes() {
    val gson by inject<Gson>()
    val recipeRepository by inject<RecipeRepository>()

    authenticate(AUTH_SESSION) {
        post<RecipeRoutes.Suggest> {
            try {
                val requestType = genericType<Request<RecipeDTO>>()
                val requestRaw = call.receive<String>()
                val request = gson.fromJson<Request<RecipeDTO>>(requestRaw, requestType)
                val newRecipe = recipeRepository.addRecipe(request.content)
//                val max = 10000
//                var curr = 0
//                while (curr < max) {
//                    curr++
//                    newRecipe = recipeRepository.submitRecipe(request.content)
//                }
                call.respond(Response.success("Recipe submitted successfully", newRecipe))
            } catch (e: Throwable) {
                application.log.error(e.stackTraceToString())
                respondWithFailure("Recipe submission failed", call)
            }
        }

        post<RecipeRoutes.Paginated> {
            try {
                val requestType = genericType<Request<PaginationRequest<RecipePaginationRequestFilter>>>()
                val requestRaw = call.receive<String>()
                val request = gson.fromJson<Request<PaginationRequest<RecipePaginationRequestFilter>>>(requestRaw, requestType)
                val recipesResponse = recipeRepository.getPaginatedRecipes(request.content)
                call.respond(Response.success("Get recipes success", recipesResponse))
            } catch (e: Throwable) {
                application.log.error(e.stackTraceToString())
                respondWithFailure("Get recipes failed", call)

            }
        }

        get<RecipeRoutes.Accept> { params ->
            try {
                val acceptedRecipe = recipeRepository.acceptRecipeById(params.recipeId)
                    ?: return@get respondWithFailure("Unexpected error when accepting recipe", call)
                call.respond(Response.success("Recipe accepted successfully", acceptedRecipe))
            } catch (e: Throwable) {
                application.log.error(e.stackTraceToString())
                respondWithFailure("Unexpected error when accepting recipe", call)
            }
        }
    }


}