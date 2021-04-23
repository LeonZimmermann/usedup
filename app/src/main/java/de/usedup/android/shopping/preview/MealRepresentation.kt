package de.usedup.android.shopping.preview

import de.usedup.android.shopping.data.ShoppingMeal
import de.usedup.android.utils.toDurationString

data class MealRepresentation(private val data: ShoppingMeal) {
  val nameText: String
    get() = data.meal.name
  val durationText: String
    get() = data.meal.duration.toDurationString()
  val imageName: String?
    get() = data.meal.imageName
}