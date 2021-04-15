package de.usedup.android.shopping.data

import de.usedup.android.datamodel.api.objects.Product
import java.io.Serializable

data class ShoppingProduct(
    val product: Product,
    var cartAmount: Int = product.amountToBuy
): Serializable {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as ShoppingProduct
    if (product != other.product) return false
    return true
  }

  override fun hashCode(): Int {
    return product.hashCode()
  }
}