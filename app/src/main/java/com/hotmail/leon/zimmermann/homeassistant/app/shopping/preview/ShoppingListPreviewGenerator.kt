package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import com.hotmail.leon.zimmermann.homeassistant.app.shopping.data.ShoppingMeal
import com.hotmail.leon.zimmermann.homeassistant.app.shopping.data.ShoppingProduct
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.PlannerRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product.ProductRepository

object ShoppingListPreviewGenerator {
  suspend fun generateShoppingListPreview(
    productRepository: ProductRepository,
    mealRepository: MealRepository,
    plannerRepository: PlannerRepository
  ): ShoppingListPreview {
    val productDiscrepancyList =
      productRepository.getAllProducts()
        .filter { it.discrepancy > 0 }
        .map { ShoppingProduct(it) }
        .toSet()
    val mealList = plannerRepository.getAllPlannerItems()
      .map { mealRepository.getMealForId(it.mealId) }
      .map { ShoppingMeal(it) }
      .toSet()
    return ShoppingListPreview(setOf(), productDiscrepancyList, mealList)
  }
}