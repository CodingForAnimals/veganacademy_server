package org.codingforanimals.veganacademy.features.model.data.source.impl

import org.codingforanimals.veganacademy.features.model.dao.RecipeTable
import org.codingforanimals.veganacademy.features.model.data.source.RecipeSource
import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO
import org.jetbrains.exposed.sql.insertAndGetId
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RecipeSourceImpl: RecipeSource {

    override fun submitRecipe(recipeDTO: RecipeDTO): Int? {
        var insertedId: Int?
        try {
            insertedId = RecipeTable.insertAndGetId {
                it[name] = recipeDTO.name
                it[description] = recipeDTO.description
                it[categoriesId] = recipeDTO.categoriesId
            }.value
        } catch (e: Throwable) {
            LoggerFactory.getLogger(this::class.java).error(e.toString())
            insertedId = null
        }
        return insertedId
    }
}