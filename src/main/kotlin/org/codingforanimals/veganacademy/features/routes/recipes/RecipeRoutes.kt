package org.codingforanimals.veganacademy.features.routes.recipes

import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.features.model.repository.RecipeRepository
import org.codingforanimals.veganacademy.features.routes.common.Request
import org.codingforanimals.veganacademy.features.routes.common.Response
import org.codingforanimals.veganacademy.utils.genericType
import org.koin.ktor.ext.inject


@KtorExperimentalLocationsAPI
@Location("recipe")
class RecipeRoutes {

    @Location("/paginated")
    class Paginated(val parent: RecipeRoutes)

    @Location("/submit")
    class Submit(val parent: RecipeRoutes)

}

@KtorExperimentalLocationsAPI
fun Route.recipeRoutes() {
    val recipeRepository by inject<RecipeRepository>()

    val gson = Gson()

    post<RecipeRoutes.Submit> {
        try {
            val requestType = genericType<Request<RecipeDTO>>()
            val requestRaw = call.receive<String>()
            val request = gson.fromJson<Request<RecipeDTO>>(requestRaw, requestType)

            val newRecipe = recipeRepository.submitRecipe(request.content)
            check(newRecipe != null) { "Recipe submission failed" }

            call.respond(Response.success("Recipe submitted successfully", newRecipe))

        } catch (e: Throwable) {
            application.log.error(e.stackTraceToString())
            call.respond(HttpStatusCode.InternalServerError, Response.failure<Any>("Recipe submission failed"))
        }
    }

}