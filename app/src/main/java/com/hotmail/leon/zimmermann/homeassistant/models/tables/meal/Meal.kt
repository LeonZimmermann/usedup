package com.hotmail.leon.zimmermann.homeassistant.models.tables.meal

data class Meal(
    val id: Long,
    val name: String,
    val duration: Int? = null,
    val description: String? = null,
    val instructions: String? = null,
    val backgroundUrl: String? = null,
    val ingredients: List<MealIngredient>
) {
    constructor(mealWithIngredients: MealWithIngredients) : this(
        mealWithIngredients.meal.id,
        mealWithIngredients.meal.name,
        mealWithIngredients.meal.duration,
        mealWithIngredients.meal.description,
        mealWithIngredients.meal.instructions,
        mealWithIngredients.meal.backgroundUrl,
        mealWithIngredients.ingredients.map {
            MealIngredient(it.productId, it.value, it.measureId)
        }
    )
}

data class MealIngredient(
    val productId: Long,
    val value: Double,
    val measureId: Long
)