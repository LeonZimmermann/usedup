package com.hotmail.leon.zimmermann.homeassistant.app.shopping

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Meal
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.toMeasure
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.math.ceil

class ShoppingListBuilder(
  private val productRepository: ProductRepository,
  private val measureRepository: MeasureRepository
) {

  private val list = mutableListOf<ShoppingProduct>()

  fun addDiscrepancies(): ShoppingListBuilder {
    runBlocking(Dispatchers.Default) {
      productRepository.getAllProducts()
        .filter { it.discrepancy > 0 }
        .forEach { addProduct(it, it.discrepancy) }
    }
    return this
  }

  fun addProduct(product: Product, cartAmount: Int): ShoppingListBuilder {
    val productInList = list.firstOrNull { it.product == product }
    if (productInList == null) list.add(ShoppingProduct(product, cartAmount))
    else productInList.cartAmount += cartAmount
    return this
  }

  fun addMeal(meal: Meal): ShoppingListBuilder {
    runBlocking(Dispatchers.Default) {
      meal.ingredients.forEach {
        val product = productRepository.getProductForId(it.productId)
        val measure = measureRepository.getMeasureForId(it.measureId)
        val cartAmount = ceil(product.capacity.toMeasure(measure) / it.value).toInt()
        addProduct(product, cartAmount)
      }
    }
    return this
  }

  fun build(): List<ShoppingProduct> {
    return list
  }

}