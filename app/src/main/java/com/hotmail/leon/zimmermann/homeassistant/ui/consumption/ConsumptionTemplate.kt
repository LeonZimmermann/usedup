package com.hotmail.leon.zimmermann.homeassistant.ui.consumption

import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity

data class ConsumptionTemplate(
    val product: ProductEntity,
    val value: Double,
    val measure: Measure
)