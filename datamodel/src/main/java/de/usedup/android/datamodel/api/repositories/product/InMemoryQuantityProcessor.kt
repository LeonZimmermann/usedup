package de.usedup.android.datamodel.api.repositories.product

import de.usedup.android.datamodel.api.objects.Product

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