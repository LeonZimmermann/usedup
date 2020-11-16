package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import com.hotmail.leon.zimmermann.homeassistant.app.shopping.data.ShoppingMeal
import com.hotmail.leon.zimmermann.homeassistant.app.shopping.data.ShoppingProduct

data class ShoppingListPreview(
  val additionalProductList: List<ShoppingProduct>,
  val productDiscrepancyList: List<ShoppingProduct>,
  val mealList: List<ShoppingMeal>
)