package de.usedup.android.shopping.preview

import de.usedup.android.shopping.data.ShoppingMeal
import de.usedup.android.shopping.data.ShoppingProduct
import de.usedup.android.datamodel.api.repositories.MealRepository
import de.usedup.android.datamodel.api.repositories.PlannerRepository
import de.usedup.android.datamodel.api.repositories.product.ProductRepository

class ShoppingListPreviewGenerator(
  private val productRepository: ProductRepository,
  private val mealRepository: MealRepository,
  private val plannerRepository: PlannerRepository
) {
  suspend fun generateShoppingListPreview(): ShoppingListPreview {
    val productDiscrepancyList =
      productRepository.getAllProducts()
        .filter { it.discrepancy > 0 }
        .map { ShoppingProduct(it) }
        .toSet()
    val mealList = plannerRepository.getAllPlannerItems()
      .mapNotNull { mealRepository.getMealForId(it.mealId) }
      .map { ShoppingMeal(it) }
      .toSet()
    return ShoppingListPreview(setOf(), productDiscrepancyList, mealList)
  }
}