package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import com.hotmail.leon.zimmermann.homeassistant.app.shopping.data.ShoppingMeal
import com.hotmail.leon.zimmermann.homeassistant.app.shopping.data.ShoppingProduct

class ShoppingListPreview(
  additionalProductList: Set<ShoppingProduct>,
  productDiscrepancyList: Set<ShoppingProduct>,
  mealList: Set<ShoppingMeal>
) {
  val additionalProductList: MutableSet<ShoppingProduct> = additionalProductList.toMutableSet()
  val productDiscrepancyList: MutableSet<ShoppingProduct> = productDiscrepancyList.toMutableSet()
  val mealList: MutableSet<ShoppingMeal> = mealList.toMutableSet()
}