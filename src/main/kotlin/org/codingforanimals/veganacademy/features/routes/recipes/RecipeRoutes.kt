package org.codingforanimals.veganacademy.features.routes.recipes

import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.codingforanimals.veganacademy.features.model.dao.Recipe
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
            val dto = gson.fromJson<Request<RecipeDTO>>(call.receive<String>(), requestType)

            val insertedId = recipeRepository.submitRecipe(dto.request)
            check(insertedId != null) { "Recipe submission failed" }

            val recipe = Recipe[insertedId].toDto()

            call.respond(Response.success("Recipe submitted successfully", insertedId))


            call.respond(Response.success("success!", dto))
        } catch (e: Throwable) {
            application.log.error(e.toString())
            call.respond(HttpStatusCode.InternalServerError, Response.failure<String>("Recipe submission failed"))
        }
    }

}