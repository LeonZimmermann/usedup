package com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption

import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity

data class ConsumptionData(
    val product: ProductEntity,
    val value: Double,
    val measure: Measure
)