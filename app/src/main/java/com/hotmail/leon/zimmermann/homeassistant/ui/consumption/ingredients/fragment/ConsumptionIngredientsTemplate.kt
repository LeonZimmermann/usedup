package com.hotmail.leon.zimmermann.homeassistant.ui.consumption.ingredients.fragment

import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import java.io.Serializable

data class ConsumptionIngredientsTemplate(
    val product: ProductEntity,
    var value: Double,
    var measure: Measure
): Serializable