package com.hotmail.leon.zimmermann.homeassistant.datamodel

import kotlin.math.floor
import kotlin.math.max

data class Product(
    var name: String,
    var quantity: Double,
    var min: Int,
    var max: Int,
    var capacity: Double,
    var measure: Measure,
    var category: Category
) {
    val discrepancy: Int
        get() = max(max - floor(quantity).toInt(), 0)
}