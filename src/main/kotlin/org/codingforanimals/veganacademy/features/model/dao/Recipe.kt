package org.codingforanimals.veganacademy.features.model.dao

import org.codingforanimals.veganacademy.features.model.dto.RecipeDTO
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object RecipeTable : IntIdTable() {
    val name = varchar("title", 64)
    val description = varchar("description", 256)
    val categoriesId = varchar("categories_id", 16)
}

class Recipe(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Recipe>(RecipeTable)

    var name by RecipeTable.name
    var description by RecipeTable.description
    val categoriesId by RecipeTable.categoriesId
//    val cookingSteps by CookingStep referrersOn CookingStepTable.recipeId

    fun toDto() = RecipeDTO(
        id = id.value,
        name = name,
        description = description,
        categoriesId = categoriesId
    )

}

//object CookingStepTable : IntIdTable() {
//    val recipeId = long("recipe_id")
//    val stepNumber = short("step_number")
//    val stepDescription = varchar("step_description", 256)
//}
//
//class CookingStep(id: EntityID<Int>) : IntEntity(id) {
//    companion object : IntEntityClass<CookingStep>(CookingStepTable)
//
//    var stepNumber by CookingStepTable.stepNumber
//    var stepDescription by CookingStepTable.stepDescription
//}
//
//
//object FoodCategoryTable : IntIdTable() {
//    val title = varchar("name", 64)
//}
//
//class FoodCategory(id: EntityID<Int>) : IntEntity(id) {
//    companion object : IntEntityClass<FoodCategory>(FoodCategoryTable)
//
//    var title by FoodCategoryTable.title
//}
