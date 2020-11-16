package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import com.hotmail.leon.zimmermann.homeassistant.app.shopping.data.ShoppingProduct

data class ProductDiscrepancyRepresentation(private val data: ShoppingProduct) {
  val nameString: String
    get() = data.product.name
  val discrepancyString: String
    get() = data.cartAmount.toString()
}