package com.hotmail.leon.zimmermann.homeassistant.ui.consumption.ingredients

import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity

data class IngredientsTemplate(
    val product: ProductEntity,
    val value: Double,
    val measure: Measure
)