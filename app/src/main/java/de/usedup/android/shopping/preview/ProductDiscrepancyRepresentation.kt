package de.usedup.android.shopping.preview

import de.usedup.android.shopping.data.ShoppingProduct

data class ProductDiscrepancyRepresentation(val data: ShoppingProduct) {
  val nameString: String
    get() = data.product.name
  val discrepancyString: String
    get() = data.cartAmount.toString()
}