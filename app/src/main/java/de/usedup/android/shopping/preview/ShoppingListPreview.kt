package de.usedup.android.shopping.preview

import de.usedup.android.shopping.data.ShoppingMeal
import de.usedup.android.shopping.data.ShoppingProduct

class ShoppingListPreview(
  additionalProductList: Set<ShoppingProduct>,
  productDiscrepancyList: Set<ShoppingProduct>,
  mealList: Set<ShoppingMeal>
) {
  val additionalProductList: MutableSet<ShoppingProduct> = additionalProductList.toMutableSet()
  val productDiscrepancyList: MutableSet<ShoppingProduct> = productDiscrepancyList.toMutableSet()
  val mealList: MutableSet<ShoppingMeal> = mealList.toMutableSet()
}