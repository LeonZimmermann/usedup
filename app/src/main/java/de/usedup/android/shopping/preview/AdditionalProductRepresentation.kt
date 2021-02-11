package de.usedup.android.shopping.preview

import de.usedup.android.shopping.data.ShoppingProduct

data class AdditionalProductRepresentation(val data: ShoppingProduct) {
  val nameString: String
    get() = data.product.name
  val discrepancyString: String
    get() = data.cartAmount.toString()
}