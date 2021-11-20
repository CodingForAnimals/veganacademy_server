package org.codingforanimals.veganacademy.features.model.repository.impl

import org.codingforanimals.veganacademy.features.model.data.source.RecipeSource
import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO
import org.codingforanimals.veganacademy.features.model.repository.RecipeRepository
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class RecipeRepositoryImpl(private val source: RecipeSource): RecipeRepository {

    override suspend fun submitRecipe(recipeDTO: RecipeDTO): Int? {
        return newSuspendedTransaction {
            source.submitRecipe(recipeDTO)
        }
    }
}