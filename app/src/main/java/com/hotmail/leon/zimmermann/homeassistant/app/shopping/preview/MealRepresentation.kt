package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import com.hotmail.leon.zimmermann.homeassistant.app.shopping.data.ShoppingMeal
import com.hotmail.leon.zimmermann.homeassistant.app.toIntFormat

data class MealRepresentation(private val data: ShoppingMeal) {
  val nameText: String
    get() = data.meal.name
  val durationText: String
    get() = "${data.meal.duration.toIntFormat()}min"
}