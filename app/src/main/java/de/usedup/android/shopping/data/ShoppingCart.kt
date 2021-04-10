package de.usedup.android.shopping.data

import java.io.Serializable

data class ShoppingCart(val value: MutableSet<ShoppingProduct>): Serializable