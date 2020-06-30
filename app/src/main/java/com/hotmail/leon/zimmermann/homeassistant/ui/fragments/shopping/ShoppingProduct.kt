package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping

import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product

data class ShoppingProduct(
    val product: Product,
    var cartAmount: Int = product.discrepancy,
    var checked: Boolean = false
)