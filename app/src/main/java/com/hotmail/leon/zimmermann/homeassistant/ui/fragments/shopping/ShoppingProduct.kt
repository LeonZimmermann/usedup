package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping

import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity

data class ShoppingProduct(val product: ProductEntity, var cartAmount: Int = product.discrepancy, var checked: Boolean = false)