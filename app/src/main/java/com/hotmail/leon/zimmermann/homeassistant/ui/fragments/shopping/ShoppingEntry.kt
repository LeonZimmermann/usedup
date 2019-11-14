package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping

import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import java.io.Serializable

data class ShoppingEntry(var product: ProductEntity, var amount: Int): Serializable