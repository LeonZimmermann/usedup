package de.usedup.android.shopping.preview

import de.usedup.android.datamodel.api.objects.Product
import de.usedup.android.datamodel.api.repositories.MeasureRepository
import de.usedup.android.datamodel.api.repositories.product.ProductRepository
import de.usedup.android.shopping.data.ShoppingList
import de.usedup.android.shopping.data.ShoppingMeal
import de.usedup.android.shopping.data.ShoppingProduct
import kotlin.math.ceil

class ShoppingListPreviewToShoppingListMapper(
  private val productRepository: ProductRepository,
  private val measureRepository: MeasureRepository
) {

  suspend fun mapPreviewToShoppingList(shoppingListPreview: ShoppingListPreview): ShoppingList {
    val additionalProductsCartAmount: List<Pair<Product, Double>> =
      shoppingListPreview.additionalProductList.map { it.product to (it.cartAmount * it.product.capacity) }
    val productDiscrepancyCartAmount: List<Pair<Product, Double>> =
      shoppingListPreview.productDiscrepancyList.map { it.product to (it.cartAmount * it.product.capacity) }
    val mealsCartAmount: List<Pair<Product, Double>> =
      mapShoppingMealSetToTheirJoinedIngredients(shoppingListPreview.mealList)
    val allCartAmounts = additionalProductsCartAmount + productDiscrepancyCartAmount + mealsCartAmount
    return ShoppingList(joinEntriesWithSameProduct(allCartAmounts))
  }

  /**
   * Extracts all the necessary amounts of the products, that are required for each meal in the set, and joins
   * the lists of the individual meals together to one list.
   */
  private suspend fun mapShoppingMealSetToTheirJoinedIngredients(
    shoppingMeals: Set<ShoppingMeal>): List<Pair<Product, Double>> =
    shoppingMeals
      .toList()
      .map { mapShoppingMealToItsIngredients(it) }
      .ifEmpty { return emptyList() }
      .reduce { acc, set -> set + acc }

  /**
   * Extracts all the necessary amounts of the products, that are required for the meal.
   */
  private suspend fun mapShoppingMealToItsIngredients(shoppingMeal: ShoppingMeal): List<Pair<Product, Double>> =
    shoppingMeal.meal.ingredients.map {
      val product = productRepository.getProductForId(it.productId) ?: throw Exception() // TODO Refactor exceptions
      val measure = measureRepository.getMeasureForId(it.measureId) ?: throw Exception()
      val rawAmountRequired = measure.baseFactor * it.value
      product to rawAmountRequired
    }

  /**
   * Joins together entries with the same product. The necessary amounts are added together and the resulting value is rounded
   * up to give the resulting cartAmount.
   */
  private fun joinEntriesWithSameProduct(productRequirements: List<Pair<Product, Double>>): Set<ShoppingProduct> {
    val joinedProductRequirements = mutableMapOf<Product, Double>()
    productRequirements.forEach {
      joinedProductRequirements.computeIfPresent(it.first) { _: Product, amount: Double -> amount + it.second }
      joinedProductRequirements.putIfAbsent(it.first, it.second)
    }
    return joinedProductRequirements.map { ShoppingProduct(it.key, ceil(it.value).toInt()) }.toSet()
  }
}