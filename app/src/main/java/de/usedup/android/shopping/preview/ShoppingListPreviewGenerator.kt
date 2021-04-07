package de.usedup.android.shopping.preview

import de.usedup.android.datamodel.api.objects.PlannerItem
import de.usedup.android.datamodel.api.objects.Product
import de.usedup.android.datamodel.api.repositories.MealRepository
import de.usedup.android.shopping.data.ShoppingMeal
import de.usedup.android.shopping.data.ShoppingProduct

class ShoppingListPreviewGenerator(private val mealRepository: MealRepository) {
  suspend fun generateShoppingListPreview(products: Set<Product>,
    plannerItems: List<PlannerItem>): ShoppingListPreview {
    val productDiscrepancyList = products
      .filter { it.discrepancy > 0 }
      .map { ShoppingProduct(it) }
      .toSet()
    val mealList = plannerItems
      .mapNotNull { mealRepository.getMealForId(it.mealId) }
      .map { ShoppingMeal(it) }
      .toSet()
    return ShoppingListPreview(setOf(), productDiscrepancyList, mealList)
  }
}