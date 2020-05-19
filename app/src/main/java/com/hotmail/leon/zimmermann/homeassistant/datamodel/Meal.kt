package com.hotmail.leon.zimmermann.homeassistant.datamodel

import com.hotmail.leon.zimmermann.homeassistant.models.tables.meal.MealIngredient

data class Meal(val name: String,
                val duration: Int? = null,
                val description: String? = null,
                val instructions: String? = null,
                val backgroundUrl: String? = null,
                val ingredients: List<MealIngredient>)

data class MealIngredient(
    val product: Product,
    val measure: Measure,
    val value: Double
)