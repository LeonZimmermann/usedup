package com.hotmail.leon.zimmermann.homeassistant.models.tables.meal

data class Meal(
    val name: String,
    val duration: Int? = null,
    val description: String? = null,
    val instructions: String? = null,
    val backgroundUrl: String? = null,
    val ingredients: List<MealIngredient>
)

data class MealIngredient(val productId: Long, val value: Double, val measureId: Long)