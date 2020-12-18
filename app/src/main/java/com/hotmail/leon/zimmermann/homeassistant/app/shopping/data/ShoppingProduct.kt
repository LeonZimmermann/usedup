package com.hotmail.leon.zimmermann.homeassistant.app.shopping.data

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Product
import java.io.Serializable

data class ShoppingProduct(
    val product: Product,
    var cartAmount: Int = product.discrepancy
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