package org.codingforanimals.veganacademy.server.features.model.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object RecipeTable : IntIdTable() {
    val title = varchar("title", 64)
    val description = varchar("description", 256)
    val likes = integer("likes")
    val isAccepted = bool("is_accepted")
}

class Recipe(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Recipe>(RecipeTable)

    var title by RecipeTable.title
    var description by RecipeTable.description
    var categories by FoodCategory via RecipeFoodCategoryTable
    val steps by RecipeStep referrersOn RecipeStepTable.recipe
    val ingredients by RecipeIngredient optionalReferrersOn RecipeIngredientTable.recipe
    var likes by RecipeTable.likes
    var isAccepted by RecipeTable.isAccepted

}

object RecipeStepTable : IntIdTable() {
    val recipe = reference("recipe", RecipeTable)
    val number = short("number")
    val description = varchar("description", 256)
}

class RecipeStep(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RecipeStep>(RecipeStepTable)

    var recipe by Recipe referencedOn RecipeStepTable.recipe
    var number by RecipeStepTable.number
    var description by RecipeStepTable.description
}

object RecipeIngredientTable : IntIdTable() {
    val recipe = reference("recipe", RecipeTable).nullable()
    val quantity = integer("quantity")
    val measurementUnit = varchar("measurement_unit", 16)
    val name = varchar("name", 64)
    val replacement = reference("replacement", RecipeIngredientTable).nullable()
}

class RecipeIngredient(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RecipeIngredient>(RecipeIngredientTable)

    var recipe: Recipe? by Recipe optionalReferencedOn RecipeIngredientTable.recipe
    var name by RecipeIngredientTable.name
    var quantity by RecipeIngredientTable.quantity
    var measurementUnit by RecipeIngredientTable.measurementUnit
    var replacement: RecipeIngredient? by RecipeIngredient optionalReferencedOn RecipeIngredientTable.replacement
}


object FoodCategoryTable : IntIdTable() {
    val category = varchar("category", 64)
}

class FoodCategory(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FoodCategory>(FoodCategoryTable)

    var category by FoodCategoryTable.category
}

object RecipeFoodCategoryTable: Table() {
    val category = reference("category", FoodCategoryTable)
    val recipe = reference("recipe", RecipeTable)
    override val primaryKey = PrimaryKey(category, recipe)
}
