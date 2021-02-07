package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import com.hotmail.leon.zimmermann.homeassistant.app.shopping.data.ShoppingList
import com.hotmail.leon.zimmermann.homeassistant.app.shopping.data.ShoppingMeal
import com.hotmail.leon.zimmermann.homeassistant.app.shopping.data.ShoppingProduct
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product.ProductRepository
import kotlin.math.ceil

class ShoppingListPreviewToShoppingListMapper(
  private val productRepository: ProductRepository,
  private val measureRepository: MeasureRepository
) {

  suspend fun mapPreviewToShoppingList(shoppingListPreview: ShoppingListPreview): ShoppingList {
    return ShoppingList(joinSameProductRequirements(getProductAmountRequirements(shoppingListPreview)))
  }

  private fun joinSameProductRequirements(productRequirements: List<Pair<Product, Double>>): Set<ShoppingProduct> {
    val joinedProductRequirements = mutableMapOf<Product, Double>()
    productRequirements.forEach {
      joinedProductRequirements.computeIfPresent(it.first) { _: Product, amount: Double -> amount + it.second }
      joinedProductRequirements.putIfAbsent(it.first, it.second)
    }
    return joinedProductRequirements.map { ShoppingProduct(it.key, ceil(it.value).toInt()) }.toSet()
  }

  private suspend fun getProductAmountRequirements(shoppingListPreview: ShoppingListPreview): List<Pair<Product, Double>> =
    shoppingListPreview.additionalProductList.map { it.product to (it.cartAmount * it.product.capacity) } +
        shoppingListPreview.productDiscrepancyList.map { it.product to (it.cartAmount * it.product.capacity) } +
        mapShoppingMealSetToShoppingProducts(shoppingListPreview.mealList)

  private suspend fun mapShoppingMealSetToShoppingProducts(shoppingMeals: Set<ShoppingMeal>): List<Pair<Product, Double>> =
    shoppingMeals
      .toList()
      .map { mapShoppingMealToShoppingProducts(it) }
      .ifEmpty { return emptyList() }
      .reduce { acc, set -> set + acc }

  private suspend fun mapShoppingMealToShoppingProducts(shoppingMeal: ShoppingMeal): List<Pair<Product, Double>> =
    shoppingMeal.meal.ingredients.map {
      val product = productRepository.getProductForId(it.productId)
      val measure = measureRepository.getMeasureForId(it.measureId)
      val rawAmountRequired = measure.baseFactor * it.value
      product to rawAmountRequired
    }
}