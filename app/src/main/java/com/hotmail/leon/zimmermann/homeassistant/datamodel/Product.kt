package com.hotmail.leon.zimmermann.homeassistant.datamodel

import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.Category
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure

data class Product(
    var name: String,
    var quantity: Double,
    var min: Int,
    var max: Int,
    var capacity: Double,
    var measure: Measure,
    var category: Category
)