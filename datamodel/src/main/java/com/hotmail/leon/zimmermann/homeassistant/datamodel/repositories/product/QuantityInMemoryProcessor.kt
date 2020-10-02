package com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.product

import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product

class QuantityInMemoryProcessor {

  fun updateSingleProductQuantity(product: Product, updatedQuantity: Double) {
    product.quantity = updatedQuantity
  }

  fun updateMultipleProductQuantities(data: List<Pair<Product, Double>>) {
    data.forEach { (product, newQuantity) ->
      product.quantity = newQuantity
    }
  }
}