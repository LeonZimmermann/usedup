package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import com.hotmail.leon.zimmermann.homeassistant.app.shopping.data.ShoppingMeal
import com.hotmail.leon.zimmermann.homeassistant.app.shopping.data.ShoppingProduct
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.PlannerRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product.ProductRepository

object ShoppingListPreviewGenerator {
  fun generateShoppingListPreview(productRepository: ProductRepository,
    plannerRepository: PlannerRepository): ShoppingListPreview {
    val productDiscrepancyList =
      productRepository.getAllProducts().filter { it.discrepancy > 0 }.map { ShoppingProduct(it) }
    val mealList = listOf<ShoppingMeal>()
    return ShoppingListPreview(listOf(), productDiscrepancyList, mealList)
  }
}