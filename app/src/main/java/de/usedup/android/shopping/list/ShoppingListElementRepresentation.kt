package de.usedup.android.shopping.list

import de.usedup.android.datamodel.api.objects.Product

data class ShoppingListElementRepresentation(val product: Product, val cartAmount: Int, var checked: Boolean = false) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as ShoppingListElementRepresentation
    if (product != other.product) return false
    return true
  }

  override fun hashCode(): Int {
    return product.hashCode()
  }
}