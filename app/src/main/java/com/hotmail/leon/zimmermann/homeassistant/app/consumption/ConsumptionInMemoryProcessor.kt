package com.hotmail.leon.zimmermann.homeassistant.app.consumption

import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product

class ConsumptionInMemoryProcessor {

  fun updateSingleProductQuantity(product: Product, updatedQuantity: Double) {
    product.quantity = updatedQuantity
  }

  fun updateMultipleProductQuantities(data: List<Pair<Product, Double>>) {
    data.forEach { (product, newQuantity) ->
      product.quantity = newQuantity
    }
  }
}