package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Product

class InMemoryQuantityProcessor {

  fun updateSingleProductQuantity(product: Product, updatedQuantity: Double) {
    product.quantity = updatedQuantity
  }

  fun updateMultipleProductQuantities(data: List<Pair<Product, Double>>) {
    data.forEach { (product, newQuantity) ->
      product.quantity = newQuantity
    }
  }
}