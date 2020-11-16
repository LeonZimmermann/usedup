package com.hotmail.leon.zimmermann.homeassistant.app.shopping.data

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Product

data class ShoppingProduct(
    val product: Product,
    var cartAmount: Int = product.discrepancy
)